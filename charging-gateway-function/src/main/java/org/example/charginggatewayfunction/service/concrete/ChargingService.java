package org.example.charginggatewayfunction.service.concrete;

import com.ramobeko.kafka.CGFKafkaMessage;
import org.example.charginggatewayfunction.model.oracle.PersonalUsage;
import org.example.charginggatewayfunction.repository.OraclePersonalUsageRepository;
import org.example.charginggatewayfunction.service.abstrct.IChargingService;
import org.example.charginggatewayfunction.util.mapper.PersonalUsageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ChargingService implements IChargingService {

    private static final Logger logger = LoggerFactory.getLogger(ChargingService.class);

    private final OraclePersonalUsageRepository personalUsageRepository;

    public ChargingService(OraclePersonalUsageRepository personalUsageRepository) {
        this.personalUsageRepository = personalUsageRepository;
    }

    @Override
    public void processCGWMessage(CGFKafkaMessage message) {
        logger.info("🚀 Processing CGWKafkaMessage: {}", message);

        // CGWKafkaMessage -> PersonalUsage dönüşümü
        PersonalUsage personalUsage = PersonalUsageMapper.mapToPersonalUsage(message);

        // Veritabanına kaydetme
        personalUsageRepository.save(personalUsage);

        logger.info("🎉 PersonalUsage kaydedildi. ID: {}", personalUsage.getPersonalUsageId());
    }
}
