package org.example.accountbalancemanagementfunction.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.ABMFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.accountbalancemanagementfunction.exception.BalanceNotFoundException;
import org.example.accountbalancemanagementfunction.exception.InvalidUsageAmountException;
import org.example.accountbalancemanagementfunction.exception.SubscriberNotFoundException;
import org.example.accountbalancemanagementfunction.exception.UnknownUsageTypeException;
import org.example.accountbalancemanagementfunction.model.oracle.OracleBalance;
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

    // Tüm UsageHandler implementasyonları Spring tarafından taranıp buraya enjekte edilir
    private final List<UsageHandler> usageHandlers;

    @Override
    @Transactional
    public void updateBalance(ABMFKafkaMessage message) {
        // 1) Kafka mesajından telefon numarasını çek
        String phoneNumber = String.valueOf(message.getSenderSubscNumber());
        double usageAmount = message.getUsageAmount();
        UsageType usageType = message.getUsageType();

        logger.info("🔔 [updateBalance] Gelen Kafka mesajı: phoneNumber={}, usageType={}, usageAmount={}",
                phoneNumber, usageType, usageAmount);

        // 2) Subscriber bul
        var subscriber = subscriberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    logger.error("❌ [updateBalance] Subscriber bulunamadı (phoneNumber={})", phoneNumber);
                    // Domain'e özel bir hata:
                    throw new SubscriberNotFoundException("Subscriber not found in DB with phoneNumber: " + phoneNumber);
                });
        logger.info("🔎 [updateBalance] Subscriber bulundu: {}", subscriber);

        Long subscId = subscriber.getId();

        // 3) Balance bul
        OracleBalance balance = balanceRepository.findBalanceBySubscriberId(subscId)
                .orElseThrow(() -> {
                    logger.error("❌ [updateBalance] Balance kaydı bulunamadı (subscId={})", subscId);
                    // Domain'e özel bir hata:
                    throw new BalanceNotFoundException("Balance not found in DB for subscriber: " + subscId);
                });
        logger.info("📄 [updateBalance] Mevcut balance kaydı: {}", balance);

        // 4) usageAmount pozitif mi?
        if (usageAmount <= 0) {
            logger.error("⚠️ [updateBalance] Kullanım miktarı 0 veya negatif olamaz. usageAmount={}", usageAmount);
            // Domain'e özel bir hata:
            throw new InvalidUsageAmountException("Usage amount must be positive, given: " + usageAmount);
        }

        // 5) Uygun stratejiyi (UsageHandler) bul
        UsageHandler handler = usageHandlers.stream()
                .filter(h -> h.supports(usageType))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("❓ [updateBalance] No UsageHandler found for usageType={}", usageType);
                    // Domain'e özel bir hata:
                    throw new UnknownUsageTypeException("No handler found for usage type: " + usageType);
                });

        // 6) Stratejiyi uygula (bakiyeden düşme veya hata)
        // Bu aşamada, UsageHandler implementasyonu içinde
        // yetersiz bakiye (InsufficientBalanceException) fırlatılabilir.
        handler.handle(balance, usageAmount);

        // 7) Kaydet
        balanceRepository.save(balance);
        logger.info("💾 [updateBalance] Balance kaydı güncellendi ve kaydedildi (subscId={})", subscId);

        logger.info("🎉 [updateBalance] Subscriber (subscId={}) balance updated successfully.", subscId);
    }
}
