package org.example.onlinechargingsystem.config;

import org.example.onlinechargingsystem.service.abstrct.IBalanceService;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.service.abstrct.IKafkaProducerService;

public class OcsWorkerConfig {
    private final IBalanceService balanceService;
    private final IgniteSubscriberRepository igniteSubscriberRepository;
    private final IKafkaProducerService kafkaProducerService;
    private final String cgfTopic;

    public OcsWorkerConfig(IBalanceService balanceService, IgniteSubscriberRepository igniteSubscriberRepository, IKafkaProducerService kafkaProducerService, String cgfTopic) {
        this.balanceService = balanceService;
        this.igniteSubscriberRepository = igniteSubscriberRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.cgfTopic = cgfTopic;
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
        return cgfTopic;
    }
}
