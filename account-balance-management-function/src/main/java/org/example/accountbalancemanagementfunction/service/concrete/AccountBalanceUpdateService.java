package org.example.accountbalancemanagementfunction.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.ABMFKafkaMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.accountbalancemanagementfunction.exception.InsufficientBalanceException;
import org.example.accountbalancemanagementfunction.repository.oracle.OracleBalanceRepository;
import org.example.accountbalancemanagementfunction.repository.oracle.OracleSubscriberRepository;
import org.example.accountbalancemanagementfunction.service.abstrct.IAccountBalanceUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountBalanceUpdateService implements IAccountBalanceUpdateService {

    private static final Logger logger = LogManager.getLogger(AccountBalanceUpdateService.class);

    private final OracleSubscriberRepository subscriberRepository;
    private final OracleBalanceRepository balanceRepository;

    @Override
    @Transactional
    public void updateBalance(ABMFKafkaMessage message) {
        // Burada subscriberNumber aslında "phone number" olarak yorumlanıyor
        String phoneNumber = String.valueOf(message.getSenderSubscNumber());
        double usageAmount = message.getUsageAmount();
        UsageType usageType = message.getUsageType();

        logger.info("Gelen Kafka mesajı: phoneNumber={}, usageType={}, usageAmount={}",
                phoneNumber, usageType, usageAmount);

        // 1) Subscriber var mı diye kontrol (telefon numarası üzerinden)
        var subscriber = subscriberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> {
                    logger.error("Subscriber bulunamadı (phoneNumber={})", phoneNumber);
                    return new RuntimeException("Subscriber not found in Oracle DB with phoneNumber: " + phoneNumber);
                });

        logger.info("Subscriber bulundu: {}", subscriber);

        // subscriber tablosunda ID alanını alıyoruz (ör: getSubscId)
        Long subscId = subscriber.getId();

        // 2) Balance kaydını al (subscId üzerinden)
        var balance = balanceRepository.findBalanceBySubscriberId(subscId)
                .orElseThrow(() -> {
                    logger.error("Balance kaydı bulunamadı (subscId={})", subscId);
                    return new RuntimeException("Balance not found in Oracle DB for subscriber: " + subscId);
                });


        logger.info("Mevcut balance kaydı: {}", balance);

        // 2.1) usageAmount pozitif mi kontrol
        if (usageAmount <= 0) {
            logger.error("Kullanım miktarı 0 veya negatif olamaz. usageAmount={}", usageAmount);
            throw new RuntimeException("Usage amount must be positive");
        }

        // 3) usageType'a göre ilgili alanı düş
        switch (usageType) {
            case MINUTE:
                if (balance.getLevelData() < usageAmount) {
                    throw new InsufficientBalanceException("Insufficient minute balance");
                }

                balance.setLevelMinutes(balance.getLevelMinutes() - (int) usageAmount);
                logger.info("Dakika bakiyesi güncellendi: yeni seviye={}", balance.getLevelMinutes());
                break;

            case SMS:
                if (balance.getLevelSms() < usageAmount) {
                    logger.error("Yetersiz SMS bakiyesi. Gerekli={}, Mevcut={}",
                            usageAmount, balance.getLevelSms());
                    throw new InsufficientBalanceException("Insufficient sms balance");
                }
                balance.setLevelSms(balance.getLevelSms() - (int) usageAmount);
                logger.info("SMS bakiyesi güncellendi: yeni seviye={}", balance.getLevelSms());
                break;

            case DATA:
                if (balance.getLevelData() < usageAmount) {
                    logger.error("Yetersiz data bakiyesi. Gerekli={}, Mevcut={}",
                            usageAmount, balance.getLevelData());
                    throw new InsufficientBalanceException("Insufficient data balance");
                }
                balance.setLevelData(balance.getLevelData() - (int) usageAmount);
                logger.info("Data bakiyesi güncellendi: yeni seviye={}", balance.getLevelData());
                break;

            default:
                logger.error("Bilinmeyen kullanım tipi: {}", usageType);
                throw new RuntimeException("Unknown usage type: " + usageType);
        }

        // 4) Güncellenmiş balance kaydını veritabanına kaydet
        balanceRepository.save(balance);
        logger.info("Balance kaydı başarıyla güncellendi ve kaydedildi (subscId={})", subscId);

        // 5) Ek loglama veya bildirim
        logger.info("Subscriber (subscId={}) balance updated successfully.", subscId);
    }
}

