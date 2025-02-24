package org.example.onlinechargingsystem.service.concrete;

import org.example.onlinechargingsystem.model.entity.Balance;
import org.example.onlinechargingsystem.repository.ignite.IgniteBalanceRepository;
import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.example.onlinechargingsystem.model.kafka.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService implements IBalanceService {

    @Autowired
    private IgniteBalanceRepository balanceRepository;

    @Autowired
    private IKafkaProducerService kafkaProducerService;

    private static final String KAFKA_TOPIC = "usage-events";

    @Override
    public Balance getBalance(Long subscriberId) {
        return balanceRepository.findById(subscriberId) // 🔹 **Metot düzeltildi**
                .orElseThrow(() -> new RuntimeException("Balance not found"));
    }

    @Override
    public void deductBalance(Long subscriberId, int minutes, int sms, int data) {
        Balance balance = getBalance(subscriberId);

        if (balance.getLevelMinutes() < minutes || balance.getLevelSms() < sms || balance.getLevelData() < data) {
            throw new RuntimeException("Insufficient balance");
        }

        // Bakiyeden düşme işlemi
        balance.setLevelMinutes(balance.getLevelMinutes() - minutes);
        balance.setLevelSms(balance.getLevelSms() - sms);
        balance.setLevelData(balance.getLevelData() - data);
        balanceRepository.save(balance);

        // Kafka'ya Kullanım Verisi Gönderme
        if (minutes > 0) {
            kafkaProducerService.sendUsageData(KAFKA_TOPIC, new KafkaMessage(subscriberId, "VOICE", minutes));
        }
        if (sms > 0) {
            kafkaProducerService.sendUsageData(KAFKA_TOPIC, new KafkaMessage(subscriberId, "SMS", sms));
        }
        if (data > 0) {
            kafkaProducerService.sendUsageData(KAFKA_TOPIC, new KafkaMessage(subscriberId, "DATA", data));
        }
    }
}
