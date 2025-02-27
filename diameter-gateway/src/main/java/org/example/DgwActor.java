package org.example;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.javadsl.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;
import com.ramobeko.akka.Command.UsageData;
import com.ramobeko.akka.ServiceKeys;

import java.util.Set;

/**
 * DGW (Data Gateway) aktörü. UsageData mesajlarını alır,
 * kullanıcıyı doğrular ve OCS Router aktörüne gönderir.
 */
public class DgwActor extends AbstractBehavior<Command> {

    // Hazelcast üzerinden kullanıcı doğrulama vb.
    private final HazelcastInstance hazelcastInstance;

    // OCS tarafındaki router aktörü (bulunca saklarız).
    private ActorRef<UsageData> ocsRouter = null;

    /**
     * Factory method (Akka önerisi).
     * @param hazelcastInstance Hazelcast bağlantısı
     * @return DgwActor Behavior
     */
    public static Behavior<Command> create(HazelcastInstance hazelcastInstance) {
        return Behaviors.setup(context -> {
            // (İsteğe bağlı) DGW'yi Receptionist'e kaydedelim:
            context.getSystem().receptionist().tell(
                    Receptionist.register(ServiceKeys.DGW_ACTOR_KEY, context.getSelf())
            );

            return new DgwActor(context, hazelcastInstance);
        });
    }

    private DgwActor(ActorContext<Command> context, HazelcastInstance hazelcastInstance) {
        super(context);
        this.hazelcastInstance = hazelcastInstance;

        // OCS Router'ın ServiceKey'ine abone olmak için:
        ActorRef<Receptionist.Listing> listingResponseAdapter =
                context.messageAdapter(Receptionist.Listing.class, OcsRouterListing::new);

        // Artık direkt OcsRouterActor yerine, ortak ServiceKeys üstünden abone oluyoruz
        context.getSystem().receptionist().tell(
                Receptionist.subscribe(ServiceKeys.OCS_ROUTER_KEY, listingResponseAdapter)
        );
    }

    /**
     * Aktörün aldığı mesajlar:
     * - UsageData (asıl iş)
     * - OcsRouterListing (receptionist’ten gelen sonuç)
     */
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(UsageData.class, this::onUsageData)
                .onMessage(OcsRouterListing.class, this::onOcsRouterListing)
                .build();
    }

    /**
     * UsageData mesajını aldığımızda:
     * 1. Kullanıcıyı Hazelcast ile doğrular
     * 2. Doğruysa, OCS Router'a iletir
     */
    private Behavior<Command> onUsageData(UsageData data) {
        IMap<String, Long> userCache = hazelcastInstance.getMap("subscriberCache");
        String actorId = getContext().getSelf().path().name();

        getContext().getLog().info("🔥 [{}] Gelen UsageData -> Kullanıcı: {}", actorId, data.getUserId());

        if (userCache.containsKey(data.getUserId())) {
            getContext().getLog().info("✅ [{}] Kullanıcı doğrulandı: {}", actorId, data.getUserId());
            getContext().getLog().info("🔹 [{}] Service Type: {}", actorId, data.getServiceType());
            getContext().getLog().info("🔹 [{}] Usage Amount: {}", actorId, data.getUsageAmount());

            if (ocsRouter != null) {
                // OCS Router'a forward
                ocsRouter.tell(data);
            } else {
                getContext().getLog().info(
                        "❌ [{}] Henüz OCS Router bulunamadı, veri iletilemedi. Data: {}",
                        actorId, data
                );
            }
        } else {
            getContext().getLog().info("❌ [{}] Kullanıcı bulunamadı: {}", actorId, data.getUserId());
        }
        return this;
    }

    /**
     * Receptionist'ten gelen Listing (OCS Router kaydı) bilgisini yakalıyoruz.
     * OCS Router aktörünün referansını elde edip saklıyoruz.
     */
    private Behavior<Command> onOcsRouterListing(OcsRouterListing listingMsg) {
        // ServiceKeys.OCS_ROUTER_KEY => ServiceKey<Command.UsageData>
        Set<ActorRef<UsageData>> serviceInstances =
                listingMsg.listing.getServiceInstances(ServiceKeys.OCS_ROUTER_KEY);

        // Birden fazla Router varsa basitçe ilkini alıyoruz:
        if (!serviceInstances.isEmpty()) {
            this.ocsRouter = serviceInstances.iterator().next();
            getContext().getLog().info("OCS Router aktörü bulundu: {}", ocsRouter);
        }
        return this;
    }

    /**
     * Receptionist.Listing'i sarmalayan mesaj sınıfı.
     * DgwActor, messageAdapter() ile bu sınıfı kendi mesaj tipine çevirir.
     */
    private static final class OcsRouterListing implements Command {
        final Receptionist.Listing listing;

        OcsRouterListing(Receptionist.Listing listing) {
            this.listing = listing;
        }
    }
}
