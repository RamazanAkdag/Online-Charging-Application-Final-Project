package org.example.onlinechargingsystem.akka.actor;


import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import com.ramobeko.akka.Command;
import org.example.onlinechargingsystem.model.kafka.KafkaMessage;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;

public class OcsWorkerActor extends AbstractBehavior<Command.UsageData> {
    private final Ignite ignite;
    private final IKafkaProducerService kafkaProducerService;

    public static Behavior<Command.UsageData> create(Ignite ignite, IKafkaProducerService kafkaProducerService) {
        return Behaviors.setup(context -> new OcsWorkerActor(context, ignite, kafkaProducerService));
    }

    public OcsWorkerActor(ActorContext<Command.UsageData> context, Ignite ignite, IKafkaProducerService kafkaProducerService) {
        super(context);
        this.ignite = ignite;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public Receive<Command.UsageData> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processUsageData)
                .build();
    }

    private Behavior<Command.UsageData> processUsageData(Command.UsageData data) {
        IgniteCache<String, Long> balanceCache = ignite.getOrCreateCache("subscriberBalanceCache");

        String actorId = getContext().getSelf().path().name(); // Aktörün adını al

        if (balanceCache.containsKey(data.getUserId())) {
            long currentBalance = balanceCache.get(data.getUserId());

            if (currentBalance >= data.getUsageAmount()) {
                // Bakiyeyi düş
                balanceCache.put(data.getUserId(), currentBalance - data.getUsageAmount());
                getContext().getLog().info("✅ [{}] Kullanıcı: {} | Yeni Bakiye: {}", actorId, data.getUserId(), currentBalance - data.getUsageAmount());

                // Kafka'ya mesaj gönder
                KafkaMessage kafkaMessage = new KafkaMessage(Long.parseLong(data.getUserId()), data.getServiceType(), (int) data.getUsageAmount());
                kafkaProducerService.sendUsageData("usage-events", kafkaMessage);

                getContext().getLog().info("📤 [{}] Kafka'ya gönderildi: {}", actorId, kafkaMessage);
            } else {
                getContext().getLog().info("❌ [{}] Yetersiz Bakiye! Kullanıcı: {} | Bakiye: {}", actorId, data.getUserId(), currentBalance);
            }
        } else {
            getContext().getLog().info("❌ [{}] Kullanıcı bulunamadı: {}", actorId, data.getUserId());
        }

        return this;
    }
}

