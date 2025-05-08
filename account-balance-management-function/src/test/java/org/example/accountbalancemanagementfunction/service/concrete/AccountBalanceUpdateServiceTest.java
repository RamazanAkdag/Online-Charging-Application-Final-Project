package org.example.accountbalancemanagementfunction.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.ABMFKafkaMessage;
import com.ramobeko.oracle.model.OracleBalance;
import com.ramobeko.oracle.model.OracleSubscriber;
import org.example.accountbalancemanagementfunction.repository.oracle.OracleBalanceRepository;
import org.example.accountbalancemanagementfunction.repository.oracle.OracleSubscriberRepository;
import org.example.accountbalancemanagementfunction.strategy.abstrct.UsageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class AccountBalanceUpdateServiceTest {

    @Mock
    private OracleSubscriberRepository subscriberRepository;

    @Mock
    private OracleBalanceRepository balanceRepository;

    @Mock
    private UsageHandler usageHandler;

    @InjectMocks
    private AccountBalanceUpdateService accountBalanceUpdateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // usageHandlers listesi elle verilmeli
        accountBalanceUpdateService = new AccountBalanceUpdateService(
                subscriberRepository,
                balanceRepository,
                List.of(usageHandler)
        );
    }

    @Test
    void updateBalance_shouldUpdateBalance_whenValidMessageProvided() {
        // --- Arrange ---

        // 1. Kafka mesajı mocku
        ABMFKafkaMessage message = new ABMFKafkaMessage();
        message.setSenderSubscNumber(905555555555L);
        message.setUsageAmount(10.0);
        message.setUsageType(UsageType.DATA);

        // 2. Subscriber mocku
        OracleSubscriber subscriber = new OracleSubscriber();
        subscriber.setId(1L);

        // 3. Balance mocku
        OracleBalance balance = new OracleBalance();
        balance.setId(100L);
        balance.setSubscriber(subscriber);
        balance.setLevelData(1000L); // 1000 MB başlangıç
        balance.setLevelMinutes(500L);
        balance.setLevelSms(100L);
        balance.setStartDate(new Date());

        // 4. Repository mock davranışları
        when(subscriberRepository.findByPhoneNumber("905555555555"))
                .thenReturn(Optional.of(subscriber));

        when(balanceRepository.findBalanceBySubscriberId(1L))
                .thenReturn(Optional.of(balance));

        when(usageHandler.supports(UsageType.DATA)).thenReturn(true);

        // --- Act ---
        accountBalanceUpdateService.updateBalance(message);

        // --- Assert ---
        verify(usageHandler, times(1)).handle(balance, 10.0);
        verify(balanceRepository, times(1)).save(balance);
    }
}
