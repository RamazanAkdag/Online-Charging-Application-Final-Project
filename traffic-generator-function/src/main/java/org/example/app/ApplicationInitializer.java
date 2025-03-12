package org.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.HazelcastClientManager;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.service.abstrct.ISubscriberService;
import com.ramobeko.dgwtgf.model.UsageType;

import java.util.Scanner;
import java.util.concurrent.*;

public class ApplicationInitializer {

    private static final Logger logger = LogManager.getLogger(ApplicationInitializer.class);
    private final ISubscriberService subscriberService;
    private final ITrafficGeneratorService trafficGeneratorService;
    private final HazelcastClientManager clientManager;
    private final Scanner scanner;
    private final ScheduledExecutorService scheduler;

    public ApplicationInitializer(
            HazelcastClientManager clientManager,
            ISubscriberService subscriberService,
            ITrafficGeneratorService trafficGeneratorService) {

        this.clientManager = clientManager;
        this.subscriberService = subscriberService;
        this.trafficGeneratorService = trafficGeneratorService;
        this.scanner = new Scanner(System.in);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void run() {
        logger.info("Application starting: Fetching subscribers from Hazelcast and generating traffic.");

        try {
            while (true) {
                System.out.println("\nSeçiminizi yapın:");
                System.out.println("1 - Belirli bir kullanıcı için kullanım verisi oluştur");
                System.out.println("2 - Tüm aboneler için otomatik kullanım verisi üret");
                System.out.println("3 - Çıkış");
                System.out.print("Seçiminizi girin (5 saniye içinde seçim yapılmazsa tüm kullanıcılar için otomatik üretilecek): ");
                System.out.println();

                // **Zamanlayıcı başlat**
                Future<String> futureInput = scheduler.submit(scanner::nextLine);

                try {
                    String choice = futureInput.get(5, TimeUnit.SECONDS); // **3 saniye içinde giriş bekler**

                    switch (choice) {
                        case "1":
                            handleManualTrafficGeneration();
                            break;
                        case "2":
                            generateForAllSubscribers();
                            break;
                        case "3":
                            logger.info("Uygulama kapatılıyor...");
                            return;
                        default:
                            logger.warn("Geçersiz seçim! Lütfen 1, 2 veya 3 girin.");
                    }

                } catch (TimeoutException e) {
                    logger.warn("⏳ 5 saniye içinde giriş yapılmadı, otomatik olarak tüm kullanıcılar için veri üretiliyor...");
                    generateForAllSubscribers();
                } catch (Exception e) {
                    logger.error("⚠️ Beklenmeyen hata oluştu: {}", e.getMessage(), e);
                }

                logger.debug("Sleeping for 5 seconds.");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            logger.error("Loop interrupted: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        } finally {
            logger.info("Shutting down Hazelcast client.");
            clientManager.shutdown();
            scheduler.shutdown(); // **Zamanlayıcı kapat**
            logger.info("Application terminated.");
        }
    }

    private void handleManualTrafficGeneration() {
        System.out.print("Telefon numarasını girin: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Kullanım türünü girin (MINUTE, SMS, DATA): ");
        String usageTypeStr = scanner.nextLine().toUpperCase();

        UsageType usageType;
        try {
            usageType = UsageType.valueOf(usageTypeStr);
        } catch (IllegalArgumentException e) {
            logger.error("❌ Geçersiz kullanım türü! Lütfen MINUTE, SMS veya DATA girin.");
            return;
        }

        System.out.print("Kullanım miktarını girin: ");
        int amount;
        try {
            amount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            logger.error("❌ Geçersiz miktar! Lütfen bir sayı girin.");
            return;
        }

        logger.info("📤 {} için {} {} gönderiliyor...", phoneNumber, amount, usageType);
        trafficGeneratorService.generateAndSendUsageDataForSubscriber(phoneNumber, usageType, amount);
        logger.info("✅ Kullanım verisi başarıyla gönderildi.");
    }

    private void generateForAllSubscribers() {
        logger.info("Tüm aboneler için otomatik kullanım verisi üretiliyor...");
        subscriberService.printSubscribers();
        trafficGeneratorService.generateAndSendUsageDataForAllSubscribers();
    }
}
