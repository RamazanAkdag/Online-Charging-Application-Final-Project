package org.example.controller;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import com.hazelcast.core.HazelcastInstance;
import com.ramobeko.dgwtgf.model.UsageRequest;
import com.ramobeko.akka.Command;
import org.example.akka.DgwActor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usage")
public class DGWUsageController {

    private final ActorSystem<Object> actorSystem;
    private final HazelcastInstance hazelcastInstance;
    private final ActorRef<Object> dgwActor;

    public DGWUsageController(ActorSystem<Object> actorSystem,
                              @Qualifier("primaryHazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.actorSystem = actorSystem;
        this.hazelcastInstance = hazelcastInstance;
        // DGW actor sistemde "dgw-actor" adıyla spawn ediliyor
        this.dgwActor = actorSystem.systemActorOf(DgwActor.create(hazelcastInstance),
                "dgw-actor",
                akka.actor.typed.Props.empty());
    }

    @PostMapping
    public ResponseEntity<String> usage(@RequestBody UsageRequest usageRequest) {
        // UsageRequest'ten Command.UsageData'ya dönüştürme işlemi:
        // Burada receiverSubscNumber, kullanıcı ID'si (userId) olarak kullanılıyor.
        Command.UsageData usageData = new Command.UsageData(
                usageRequest.getReceiverSubscNumber(),   // userId
                usageRequest.getUsageType().toString(),   // serviceType
                (int) usageRequest.getUsageAmount()             // usageAmount
        );

        // DGW actor'e mesaj gönderiliyor
        dgwActor.tell(usageData);
        return ResponseEntity.ok("Usage data message sent to DGW actor");
    }
}
