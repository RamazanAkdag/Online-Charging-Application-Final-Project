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
        logger.info("🚀 Processing CGFKafkaMessage: {}", message);

        // 1) Giver (Gönderen) subscriber'ı telefon numarasından bul
        OracleSubscriber giver = subscriberRepository.findByPhoneNumber(message.getSenderSubscNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Giver subscriber bulunamadı! Phone: " + message.getSenderSubscNumber()));

        // 2) Receiver (Alıcı) subscriber'ı telefon numarasından bul
        OracleSubscriber receiver = subscriberRepository.findByPhoneNumber(message.getReceiverSubscNumber())
                .orElseThrow(() -> new RuntimeException(
                        "Receiver subscriber bulunamadı! Phone: " + message.getReceiverSubscNumber()));

        // 3) CGFKafkaMessage -> PersonalUsage dönüşümü
        PersonalUsage personalUsage = PersonalUsageMapper.mapToPersonalUsage(message);

        // 4) GiverId ve ReceiverId alanlarını bulduğumuz subscriber ID'leriyle set et
        personalUsage.setGiverId(giver.getId());
        personalUsage.setReceiverId(receiver.getId());

        // 5) Veritabanına kaydetme
        personalUsageRepository.save(personalUsage);

        logger.info("🎉 PersonalUsage kaydedildi. ID: {}", personalUsage.getPersonalUsageId());
    }
}
