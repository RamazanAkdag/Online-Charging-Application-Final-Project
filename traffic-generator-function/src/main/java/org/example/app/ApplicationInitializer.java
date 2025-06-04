package org.example.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.config.HazelcastClientManager;
import org.example.service.abstrct.ITrafficGeneratorService;
import org.example.service.abstrct.ISubscriberService;
import com.ramobeko.dgwtgf.model.UsageType;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class ApplicationInitializer {

    private static final Logger logger = LogManager.getLogger(ApplicationInitializer.class);
    private final ISubscriberService subscriberService;
    private final ITrafficGeneratorService trafficGeneratorService;
    private final HazelcastClientManager clientManager;
    private final Scanner scanner;
    private final ScheduledExecutorService scheduler;
    private final ExecutorService continuousUsageExecutor;
    private volatile boolean isGeneratingContinuousUsage = false;

    public ApplicationInitializer(
            HazelcastClientManager clientManager,
            ISubscriberService subscriberService,
            ITrafficGeneratorService trafficGeneratorService) {

        this.clientManager = clientManager;
        this.subscriberService = subscriberService;
        this.trafficGeneratorService = trafficGeneratorService;
        this.scanner = new Scanner(System.in);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.continuousUsageExecutor = Executors.newSingleThreadExecutor();
    }

    public void run() {
        logger.info("Application starting: Fetching subscribers from Hazelcast and generating traffic.");

        try {
            while (true) {
                // Eğer sürekli kullanım açık değilse, menüyü göster ve seçim al
                if (!isGeneratingContinuousUsage) {
                    System.out.println("\nSeçiminizi yapın:");
                    System.out.println("1 - Belirli bir kullanıcı için kullanım verisi oluştur");
                    System.out.println("2 - Tüm aboneler için otomatik kullanım verisi üret");
                    System.out.println("3 - Çıkış");
                    System.out.println("4 - Sürekli hızlı kullanım üret (Durdurmak için tekrar 4'e basın)");
                    System.out.print("Seçiminizi girin (5 saniye içinde seçim yapılmazsa tüm kullanıcılar için otomatik üretilecek): ");
                    System.out.println();

                    Future<String> futureInput = scheduler.submit(scanner::nextLine);

                    try {
                        String choice = futureInput.get(5, TimeUnit.SECONDS);

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
                            case "4":
                                toggleContinuousUsageGeneration();
                                break;
                            default:
                                logger.warn("Geçersiz seçim! Lütfen 1, 2, 3 veya 4 girin.");
                        }
                    } catch (TimeoutException e) {
                        logger.warn("⏳ 5 saniye içinde giriş yapılmadı, otomatik olarak tüm kullanıcılar için veri üretiliyor...");
                        generateForAllSubscribers();
                    } catch (Exception e) {
                        logger.error("⚠️ Beklenmeyen hata oluştu: {}", e.getMessage(), e);
                    }
                } else {
                    // Eğer sürekli kullanım açık ise, beklemeden devam et
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Loop interrupted: {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage(), e);
        } finally {
            logger.info("Shutting down Hazelcast client.");
            clientManager.shutdown();
            scheduler.shutdown();
            continuousUsageExecutor.shutdown();
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

    private void toggleContinuousUsageGeneration() {
        if (isGeneratingContinuousUsage) {
            logger.info("🚫 Sürekli kullanım üretimi durduruluyor...");
            isGeneratingContinuousUsage = false;
        } else {
            logger.info("🔄 Sürekli kullanım üretimi başlatılıyor...");
            isGeneratingContinuousUsage = true;
            continuousUsageExecutor.submit(this::generateContinuousUsage);
        }
    }

    private void generateContinuousUsage() {
        Random random = new Random();

        while (isGeneratingContinuousUsage) {
            try {
                List<String> subscriberNumbers = subscriberService.getAllSubscriberNumbers();
                if (subscriberNumbers.isEmpty()) {
                    logger.warn("🚫 Hiç abone bulunamadı!");
                    return;
                }

                // Rastgele bir abone seç
                String phoneNumber = subscriberNumbers.get(random.nextInt(subscriberNumbers.size()));

                // Rastgele kullanım türü ve miktar seç
                UsageType usageType = UsageType.values()[random.nextInt(UsageType.values().length)];
                int amount = random.nextInt(50) + 1; // 1 ile 50 arasında rastgele miktar

                logger.info("📤 Sürekli kullanım: {} için {} {}", phoneNumber, amount, usageType);
                trafficGeneratorService.generateAndSendUsageDataForSubscriber(phoneNumber, usageType, amount);

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                logger.error("Sürekli kullanım üretimi kesildi.", e);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                logger.error("⚠️ Kullanım üretimi sırasında hata oluştu: {}", e.getMessage(), e);
            }
        }

        logger.info("✅ Sürekli kullanım üretimi tamamlandı.");
    }
}
