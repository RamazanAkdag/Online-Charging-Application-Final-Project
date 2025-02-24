package org.example;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.Materializer;
import akka.stream.SystemMaterializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;
import java.util.concurrent.CompletionStage;

public class DgwActor extends AbstractBehavior<Command> {
    private final HazelcastInstance hazelcastInstance;
    private final Http http;
    private final Materializer materializer;
    private final ObjectMapper objectMapper;

    private static final String OCS_URL = "http://localhost:8080/ocs/usage"; // OCS API URL

    private DgwActor(ActorContext<Command> context, HazelcastInstance hazelcastInstance) {
        super(context);
        this.hazelcastInstance = hazelcastInstance;
        this.http = Http.get(context.getSystem());
        this.materializer = SystemMaterializer.get(context.getSystem()).materializer();
        this.objectMapper = new ObjectMapper();
    }

    public static Behavior<Command> create(HazelcastInstance hazelcastInstance) {
        return Behaviors.setup(context -> new DgwActor(context, hazelcastInstance));
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processUsageData)
                .build();
    }

    private Behavior<Command> processUsageData(Command.UsageData data) {
        IMap<String, Long> userCache = hazelcastInstance.getMap("subscriberCache");

        String actorId = getContext().getSelf().path().name();
        System.out.println("🔥 [" + actorId + "] Baba merhaba!");

        if (userCache.containsKey(data.getUserId())) {
            getContext().getLog().info("✅ [{}] Kullanıcı doğrulandı: {}", actorId, data.getUserId());
            getContext().getLog().info("🔹 [{}] Kullanım Türü: {}", actorId, data.getServiceType());
            getContext().getLog().info("🔹 [{}] Kullanım Miktarı: {}", actorId, data.getUsageAmount());

            // ✅ OCS API'ye HTTP POST isteği gönder
            sendUsageDataToOcs(data);
        } else {
            getContext().getLog().info("❌ [{}] Kullanıcı bulunamadı: {}", actorId, data.getUserId());
        }

        return this;
    }

    private void sendUsageDataToOcs(Command.UsageData data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            HttpRequest request = HttpRequest.POST(OCS_URL)
                    .withEntity(HttpEntities.create(ContentTypes.APPLICATION_JSON, jsonData));

            // ❌ Hata: http.singleRequest(request, materializer);
            // ✅ Doğru: materializer kullanmadan tek parametre ile çağır
            CompletionStage<HttpResponse> response = http.singleRequest(request);

            response.thenAccept(res -> {
                System.out.println("📡 OCS'ye istek gönderildi, HTTP Yanıt Kodu: " + res.status());
                if (res.status().isSuccess()) {
                    System.out.println("✅ OCS başarılı yanıt verdi!");
                } else {
                    System.out.println("❌ OCS isteği başarısız oldu!");
                }
            });
        } catch (Exception e) {
            System.out.println("⚠️ OCS'ye veri gönderirken hata oluştu: " + e.getMessage());
        }
    }

}
