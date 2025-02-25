package org.example.http;


import com.ramobeko.akka.Command;

public interface TrafficSender {
    void sendUsageData(Command.UsageData data);
}

