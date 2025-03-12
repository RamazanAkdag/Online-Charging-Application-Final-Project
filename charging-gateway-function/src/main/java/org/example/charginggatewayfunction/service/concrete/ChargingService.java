package org.example.charginggatewayfunction.service.concrete;

import com.ramobeko.kafka.message.CGFKafkaMessage;
import com.ramobeko.oracle.model.OracleSubscriber;
import com.ramobeko.oracle.model.PersonalUsage;
import org.example.charginggatewayfunction.repository.OraclePersonalUsageRepository;
import org.example.charginggatewayfunction.repository.OracleSubscriberRepository;
import org.example.charginggatewayfunction.service.abstrct.IChargingService;
import org.example.charginggatewayfunction.util.mapper.PersonalUsageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChargingService implements IChargingService {

    private static final Logger logger = LoggerFactory.getLogger(ChargingService.class);

    private final OraclePersonalUsageRepository personalUsageRepository;
    private final OracleSubscriberRepository subscriberRepository;

    public ChargingService(OraclePersonalUsageRepository personalUsageRepository,
                           OracleSubscriberRepository subscriberRepository) {
        this.personalUsageRepository = personalUsageRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public void processCGFMessage(CGFKafkaMessage message) {
        logger.info("📥 Gelen CGFKafkaMessage: {}", message);

        OracleSubscriber giver = subscriberRepository.findByPhoneNumber(message.getSenderSubscNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Giver subscriber bulunamadı! Phone: " + message.getSenderSubscNumber()));

        OracleSubscriber receiver = subscriberRepository.findByPhoneNumber(message.getReceiverSubscNumber())
                .orElse(null); // Eğer receiver yoksa, `null` olarak devam etsin.

        PersonalUsage personalUsage = PersonalUsageMapper.mapToPersonalUsage(message);

        personalUsage.setGiverId(giver.getId());
        personalUsage.setReceiverId(receiver != null ? receiver.getId() : null); // receiver `null` ise ID de `null` olur.

        personalUsageRepository.save(personalUsage);
        logger.info("🎉 PersonalUsage kaydedildi. ID: {}", personalUsage.getPersonalUsageId());
    }

}
