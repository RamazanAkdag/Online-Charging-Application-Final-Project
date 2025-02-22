package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshalling.Marshaller;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletionStage;

public class DgwHttpServer extends AllDirectives {
    private final ActorSystem system;
    private final ActorRef dgwRouter;
    private final ObjectMapper objectMapper;

    public DgwHttpServer(ActorSystem system, ActorRef dgwRouter) {
        this.system = system;
        this.dgwRouter = dgwRouter;
        this.objectMapper = new ObjectMapper(); // Jackson ObjectMapper
    }

    public Route createRoute() {
        return post(() ->
                entity(Unmarshaller.entityToString(), json -> {
                    try {
                        UsageData data = objectMapper.readValue(json, UsageData.class);
                        dgwRouter.tell(data, ActorRef.noSender());
                        return complete(HttpResponse.create()
                                .withEntity(HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8, "Data received")));
                    } catch (Exception e) {
                        return complete(HttpResponse.create()
                                .withEntity(HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8, "Invalid Data")));
                    }
                })
        );
    }

    public void startHttpServer() {
        CompletionStage<ServerBinding> binding = Http.get(system)
                .newServerAt("127.0.0.1", 9091) // 9091 PORTUNDA ÇALIŞACAK
                .bind(createRoute());

        binding.thenAccept(serverBinding ->
                System.out.println("Server started at http://127.0.0.1:9091")
        );
    }
}
