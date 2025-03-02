/*package org.example.onlinechargingsystem.controller;


import com.ramobeko.akka.Command;
import akka.actor.typed.ActorRef;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ocs")
public class OcsController {
    private final ActorRef<Command.UsageData> ocsRouter;

    public OcsController(ActorRef<Command.UsageData> ocsRouter) {
        this.ocsRouter = ocsRouter;
    }

    @PostMapping("/usage")
    public ResponseEntity<String> processUsage(@RequestBody Command.UsageData data) {
        // Akka Router'a mesaj gönder
        ocsRouter.tell(new Command.UsageData(data.getUserId(), data.getServiceType(), data.getUsageAmount()));


        return ResponseEntity.ok("Usage data sent to OCS successfully.");
    }
}
*/

