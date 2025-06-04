package org.example.onlinechargingsystem.service.concrete;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.ignite.IgniteBalance;
import com.ramobeko.ignite.IgniteSubscriber;
import com.ramobeko.kafka.message.NFKafkaMessage;
import org.example.onlinechargingsystem.repository.ignite.IgniteSubscriberRepository;
import org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageBalanceChecker;
import org.example.onlinechargingsystem.strategy.balancestrategy.concrete.checker.UsageBalanceCheckerFactory;
import org.example.onlinechargingsystem.strategy.balancestrategy.concrete.deduction.UsageDeductionStrategyFactory;
import org.example.onlinechargingsystem.strategy.thresholdstrategy.concrete.UsageThresholdPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceServiceTest {

    private IgniteSubscriberRepository igniteSubscriberRepository;
    private BalanceService balanceService;
    private UsageThresholdPolicy thresholdPolicy;

    @BeforeEach
    void setUp() {
        igniteSubscriberRepository = mock(IgniteSubscriberRepository.class);
        thresholdPolicy = mock(UsageThresholdPolicy.class);
        balanceService = new BalanceService(igniteSubscriberRepository, thresholdPolicy);
    }

    @Test
    void evaluateUsageThreshold_shouldReturn50PercentExceeded() {
        // Arrange
        Long subscNumber = 12345L;
        double usageAmount = 50.0;
        UsageType usageType = UsageType.DATA;

        IgniteBalance balance = mock(IgniteBalance.class);
        IgniteSubscriber subscriber = mock(IgniteSubscriber.class);

        when(subscriber.getBalances()).thenReturn(Collections.singletonList(balance));
        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.of(subscriber));

        IUsageBalanceChecker checker = mock(IUsageBalanceChecker.class);
        when(checker.getAvailableBalance(balance)).thenReturn(100.0); // toplam 100 birim

        when(thresholdPolicy.evaluate(eq(subscNumber), eq(usageType), anyDouble()))
                .thenReturn(Optional.of(new NFKafkaMessage(
                        "50% Threshold Exceeded",
                        String.valueOf(subscNumber),
                        Instant.now()
                )));

        try (MockedStatic<UsageBalanceCheckerFactory> utilities = mockStatic(UsageBalanceCheckerFactory.class)) {
            utilities.when(() -> UsageBalanceCheckerFactory.getChecker(usageType)).thenReturn(checker);

            // Act
            String result = balanceService.evaluateUsageThreshold(subscNumber, usageAmount, usageType);

            // Assert
            assertEquals("50% Threshold Exceeded", result);
        }
    }

    @Test
    void evaluateUsageThreshold_shouldReturn80PercentExceeded() {
        // Arrange
        Long subscNumber = 12345L;
        double usageAmount = 80.0;
        UsageType usageType = UsageType.DATA;

        IgniteBalance balance = mock(IgniteBalance.class);
        IgniteSubscriber subscriber = mock(IgniteSubscriber.class);

        when(subscriber.getBalances()).thenReturn(Collections.singletonList(balance));
        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.of(subscriber));

        IUsageBalanceChecker checker = mock(IUsageBalanceChecker.class);
        when(checker.getAvailableBalance(balance)).thenReturn(100.0);  // toplam 100 birim var

        when(thresholdPolicy.evaluate(eq(subscNumber), eq(usageType), anyDouble()))
                .thenReturn(Optional.of(new NFKafkaMessage(
                        "80% Threshold Exceeded",
                        String.valueOf(subscNumber),
                        Instant.now()
                )));

        try (MockedStatic<UsageBalanceCheckerFactory> utilities = mockStatic(UsageBalanceCheckerFactory.class)) {
            utilities.when(() -> UsageBalanceCheckerFactory.getChecker(usageType)).thenReturn(checker);

            // Act
            String result = balanceService.evaluateUsageThreshold(subscNumber, usageAmount, usageType);

            // Assert
            assertEquals("80% Threshold Exceeded", result);
        }
    }


    @Test
    void hasSufficientBalance_shouldReturnTrue_whenBalanceIsEnough() {
        // Arrange
        Long subscNumber = 12345L;
        double usageAmount = 50.0;
        UsageType usageType = UsageType.SMS;

        IgniteBalance balance = mock(IgniteBalance.class);
        IgniteSubscriber subscriber = mock(IgniteSubscriber.class);

        when(subscriber.getBalances()).thenReturn(Collections.singletonList(balance));
        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.of(subscriber));

        IUsageBalanceChecker checker = mock(IUsageBalanceChecker.class);
        when(checker.getAvailableBalance(balance)).thenReturn(100.0);

        try (MockedStatic<UsageBalanceCheckerFactory> utilities = mockStatic(UsageBalanceCheckerFactory.class)) {
            utilities.when(() -> UsageBalanceCheckerFactory.getChecker(usageType)).thenReturn(checker);

            // Act
            boolean sufficient = balanceService.hasSufficientBalance(subscNumber, usageAmount, usageType);

            // Assert
            assertTrue(sufficient);
        }
    }

    @Test
    void hasSufficientBalance_shouldReturnFalse_whenBalanceIsNotEnough() {
        // Arrange
        Long subscNumber = 12345L;
        double usageAmount = 150.0;
        UsageType usageType = UsageType.SMS;

        IgniteBalance balance = mock(IgniteBalance.class);
        IgniteSubscriber subscriber = mock(IgniteSubscriber.class);

        when(subscriber.getBalances()).thenReturn(Collections.singletonList(balance));
        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.of(subscriber));

        IUsageBalanceChecker checker = mock(IUsageBalanceChecker.class);
        when(checker.getAvailableBalance(balance)).thenReturn(100.0);

        try (MockedStatic<UsageBalanceCheckerFactory> utilities = mockStatic(UsageBalanceCheckerFactory.class)) {
            utilities.when(() -> UsageBalanceCheckerFactory.getChecker(usageType)).thenReturn(checker);

            // Act
            boolean sufficient = balanceService.hasSufficientBalance(subscNumber, usageAmount, usageType);

            // Assert
            assertFalse(sufficient);
        }
    }

    @Test
    void evaluateUsageThreshold_shouldReturnNull_whenBalanceIsZero() {
        Long subscNumber = 12345L;
        double usageAmount = 10.0;
        UsageType usageType = UsageType.DATA;

        IgniteBalance balance = mock(IgniteBalance.class);
        IgniteSubscriber subscriber = mock(IgniteSubscriber.class);

        when(subscriber.getBalances()).thenReturn(Collections.singletonList(balance));
        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.of(subscriber));

        IUsageBalanceChecker checker = mock(IUsageBalanceChecker.class);
        when(checker.getAvailableBalance(balance)).thenReturn(0.0);

        try (MockedStatic<UsageBalanceCheckerFactory> utilities = mockStatic(UsageBalanceCheckerFactory.class)) {
            utilities.when(() -> UsageBalanceCheckerFactory.getChecker(usageType)).thenReturn(checker);

            String result = balanceService.evaluateUsageThreshold(subscNumber, usageAmount, usageType);

            assertNull(result);
        }
    }

    @Test
    void evaluateUsageThreshold_shouldThrowException_whenSubscriberNotFound() {
        Long subscNumber = 99999L;
        double usageAmount = 10.0;
        UsageType usageType = UsageType.MINUTE;

        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                balanceService.evaluateUsageThreshold(subscNumber, usageAmount, usageType));
    }

    @Test
    void hasSufficientBalance_shouldThrowException_whenSubscriberNotFound() {
        Long subscNumber = 99999L;
        double usageAmount = 10.0;
        UsageType usageType = UsageType.SMS;

        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                balanceService.hasSufficientBalance(subscNumber, usageAmount, usageType));
    }
    @Test
    void deductBalance_shouldCallDeductionStrategy() {
        Long subscNumber = 12345L;
        int amount = 20;
        UsageType usageType = UsageType.DATA;

        IgniteBalance balance = mock(IgniteBalance.class);
        IgniteSubscriber subscriber = mock(IgniteSubscriber.class);

        when(subscriber.getBalances()).thenReturn(Collections.singletonList(balance));
        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.of(subscriber));

        var deductionStrategy = mock(org.example.onlinechargingsystem.strategy.balancestrategy.abstrct.IUsageDeductionStrategy.class);

        try (MockedStatic<UsageDeductionStrategyFactory> utilities = mockStatic(UsageDeductionStrategyFactory.class)) {
            utilities.when(() -> UsageDeductionStrategyFactory.getStrategy(usageType)).thenReturn(deductionStrategy);

            balanceService.deductBalance(subscNumber, amount, usageType);

            verify(deductionStrategy).deductBalance(subscriber, amount, igniteSubscriberRepository, subscNumber);
        }
    }
    @Test
    void deductBalance_shouldThrowException_whenSubscriberNotFound() {
        Long subscNumber = 99999L;
        int amount = 10;
        UsageType usageType = UsageType.DATA;

        when(igniteSubscriberRepository.findById(subscNumber)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                balanceService.deductBalance(subscNumber, amount, usageType));
    }


}

