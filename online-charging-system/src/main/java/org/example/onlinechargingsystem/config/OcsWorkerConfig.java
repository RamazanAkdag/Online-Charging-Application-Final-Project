package org.example.onlinechargingsystem.config;

import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class OcsWorkerConfig {

    private final IBalanceService balanceService;
    private final IgniteSubscriberRepository igniteSubscriberRepository;
    private final IKafkaProducerService kafkaProducerService;

    @Value("${cgf.topic}")  // ✅ Spring'in application.properties'ten okumasını sağlıyoruz.
    private String cgfTopic;

    @Value("${abmf.topic}")
    private String abmfTopic;

    public OcsWorkerConfig(IBalanceService balanceService,
                           IgniteSubscriberRepository igniteSubscriberRepository,
                           IKafkaProducerService kafkaProducerService) {

        this.balanceService = balanceService;
        this.igniteSubscriberRepository = igniteSubscriberRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    public IBalanceService getBalanceService() {
        return balanceService;
    }

    public IgniteSubscriberRepository getIgniteSubscriberRepository() {
        return igniteSubscriberRepository;
    }

    public IKafkaProducerService getKafkaProducerService() {
        return kafkaProducerService;
    }

    public String getCgfTopic() {
        return Objects.requireNonNull(cgfTopic, "❌ cgf.topic is missing in application.properties!");
    }

    public String getAbmfTopic() {
        return Objects.requireNonNull(abmfTopic, "❌ abmf.topic is missing in application.properties!");
    }
}
