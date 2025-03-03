package org.example.charginggatewayfunction.service.abstrct;

import com.ramobeko.kafka.CGFKafkaMessage;

public interface IChargingService {
    // Örneğin, CGWKafkaMessage'ı alıp veritabanına kaydeden bir metot
    void processCGWMessage(CGFKafkaMessage message);
}
