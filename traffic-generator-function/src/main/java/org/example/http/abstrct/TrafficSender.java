package org.example.http.abstrct;


import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageRequest;

public interface TrafficSender {
    void sendUsageData(UsageRequest data);
}

