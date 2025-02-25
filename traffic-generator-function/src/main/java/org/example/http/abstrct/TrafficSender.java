package org.example.http.abstrct;


import com.ramobeko.akka.Command;

public interface TrafficSender {
    void sendUsageData(Command.UsageData data);
}

