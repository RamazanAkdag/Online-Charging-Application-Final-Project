package org.example;

/*import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramobeko.akka.Command;

import java.util.concurrent.CompletionStage;

public class DgwHttpServer extends AllDirectives {
    private final ActorSystem<Void> system;
    private final ActorRef<Command> dgwRouter;
    private final ObjectMapper objectMapper;

    public DgwHttpServer(ActorSystem<Void> system, ActorRef<Command> dgwRouter) {
        this.system = system;
        this.dgwRouter = dgwRouter;
        this.objectMapper = new ObjectMapper();
    }

    public Route createRoute() {
        return post(() ->
                entity(Unmarshaller.entityToString(), json -> {
                    try {
                        System.out.println("SELAMIN ALEYKUM");
                        Command.UsageData data = objectMapper.readValue(json, Command.UsageData.class);

                        System.out.println("request user id : "+data.getUserId());
                        dgwRouter.tell(data);
                        return complete(HttpResponse.create()
                                .withEntity(HttpEntities.create(ContentTypes.TEXT_PLAIN_UTF8, "Data received")));
                    } catch (Exception e) {
                        System.out.println("hatali");
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
*/