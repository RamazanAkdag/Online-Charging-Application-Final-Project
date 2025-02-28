package org.example.controller;

import akka.actor.ActorSystem;
import com.ramobeko.dgwtgf.model.UsageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usage")
public class DGWUsageController {

    @PostMapping
    public ResponseEntity<String> usage(@RequestBody UsageRequest usageRequest){

        System.out.println("selam");
        return null;
    }
}
