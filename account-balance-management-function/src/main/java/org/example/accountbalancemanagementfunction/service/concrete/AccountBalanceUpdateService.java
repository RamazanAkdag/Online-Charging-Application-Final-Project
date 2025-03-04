package org.example.accountbalancemanagementfunction.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.ABMFKafkaMessage;
import com.ramobeko.oracle.model.OracleBalance;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.accountbalancemanagementfunction.repository.oracle.OracleBalanceRepository;
import org.example.accountbalancemanagementfunction.repository.oracle.OracleSubscriberRepository;
import org.example.accountbalancemanagementfunction.service.abstrct.IAccountBalanceUpdateService;
import org.example.accountbalancemanagementfunction.strategy.abstrct.UsageHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBalanceUpdateService implements IAccountBalanceUpdateService {

    private static final Logger logger = LogManager.getLogger(AccountBalanceUpdateService.class);

    private final OracleSubscriberRepository subscriberRepository;
    private final OracleBalanceRepository balanceRepository;
    private final List<UsageHandler> usageHandlers;

    @Override
    @Transactional
    public void updateBalance(ABMFKafkaMessage message) {
        String phoneNumber = String.valueOf(message.getSenderSubscNumber());
        double usageAmount = message.getUsageAmount();
        UsageType usageType = message.getUsageType();

        logger.info("🔔 [updateBalance] Gelen Kafka mesajı: phoneNumber={}, usageType={}, usageAmount={}",
                phoneNumber, usageType, usageAmount);

        var subscriber = subscriberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    logger.error("❌ [updateBalance] Subscriber bulunamadı (phoneNumber={})", phoneNumber);
                    throw new RuntimeException("Subscriber not found in DB with phoneNumber: " + phoneNumber);
                });

        logger.info("🔎 [updateBalance] Subscriber bulundu: {}", subscriber);
        Long subscId = subscriber.getId();

        OracleBalance balance = balanceRepository.findBalanceBySubscriberId(subscId)
                .orElseThrow(() -> {
                    logger.error("❌ [updateBalance] Balance kaydı bulunamadı (subscId={})", subscId);
                    throw new RuntimeException("Balance not found in DB for subscriber: " + subscId);
                });

        logger.info("📄 [updateBalance] Mevcut balance kaydı: {}", balance);

        if (usageAmount <= 0) {
            logger.error("⚠️ [updateBalance] Kullanım miktarı 0 veya negatif olamaz. usageAmount={}", usageAmount);
            throw new RuntimeException("Usage amount must be positive, given: " + usageAmount);
        }

        UsageHandler handler = usageHandlers.stream()
                .filter(h -> h.supports(usageType))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("❓ [updateBalance] No UsageHandler found for usageType={}", usageType);
                    throw new RuntimeException("No handler found for usage type: " + usageType);
                });

        handler.handle(balance, usageAmount);
        balanceRepository.save(balance);

        logger.info("💾 [updateBalance] Balance kaydı güncellendi ve kaydedildi (subscId={})", subscId);
        logger.info("🎉 [updateBalance] Subscriber (subscId={}) balance updated successfully.", subscId);
    }
}

