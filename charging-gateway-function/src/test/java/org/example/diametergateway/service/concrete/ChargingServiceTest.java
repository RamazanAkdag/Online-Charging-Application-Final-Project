package org.example.diametergateway.service.concrete;

import com.ramobeko.kafka.message.CGFKafkaMessage;
import com.ramobeko.oracle.model.OracleSubscriber;
import com.ramobeko.oracle.model.PersonalUsage;
import org.example.charginggatewayfunction.repository.OraclePersonalUsageRepository;
import org.example.charginggatewayfunction.repository.OracleSubscriberRepository;
import com.ramobeko.dgwtgf.model.UsageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargingServiceTest {

    @Mock
    private OraclePersonalUsageRepository personalUsageRepository;

    @Mock
    private OracleSubscriberRepository subscriberRepository;

    @InjectMocks
    private org.example.charginggatewayfunction.service.concrete.ChargingService chargingService;

    @Captor
    ArgumentCaptor<PersonalUsage> usageCaptor;

    private CGFKafkaMessage message;

    @BeforeEach
    void setup() {
        message = new CGFKafkaMessage();
        message.setSenderSubscNumber("500");
        message.setReceiverSubscNumber("600");
        message.setUsageAmount(120.0); // Bu usageDuration'a dönüşecek.
        message.setUsageType(UsageType.DATA);
        message.setUsageTime(new Date());
    }

    @Test
    @DisplayName("should process message and save usage when both giver and receiver exist")
    void testProcessMessage_withGiverAndReceiver() {
        // Arrange
        OracleSubscriber giver = new OracleSubscriber();
        giver.setId(1L);

        OracleSubscriber receiver = new OracleSubscriber();
        receiver.setId(2L);

        when(subscriberRepository.findByPhoneNumber("500")).thenReturn(Optional.of(giver));
        when(subscriberRepository.findByPhoneNumber("600")).thenReturn(Optional.of(receiver));

        // Act
        chargingService.processCGFMessage(message);

        // Assert
        verify(personalUsageRepository).save(usageCaptor.capture());
        PersonalUsage saved = usageCaptor.getValue();

        assertEquals(1L, saved.getGiverId());
        assertEquals(2L, saved.getReceiverId());
        assertEquals(UsageType.DATA, saved.getUsageType());
        assertEquals(120, saved.getUsageDuration()); // usageAmount -> usageDuration
        assertNotNull(saved.getUsageDate());
        assertTrue(saved.getUsageDate() instanceof LocalDate);
    }

    @Test
    @DisplayName("should process message and save usage with null receiver if not found")
    void testProcessMessage_withNullReceiver() {
        // Arrange
        OracleSubscriber giver = new OracleSubscriber();
        giver.setId(1L);

        when(subscriberRepository.findByPhoneNumber("500")).thenReturn(Optional.of(giver));
        when(subscriberRepository.findByPhoneNumber("600")).thenReturn(Optional.empty());

        // Act
        chargingService.processCGFMessage(message);

        // Assert
        verify(personalUsageRepository).save(usageCaptor.capture());
        PersonalUsage saved = usageCaptor.getValue();

        assertEquals(1L, saved.getGiverId());
        assertNull(saved.getReceiverId());
        assertEquals(UsageType.DATA, saved.getUsageType());
        assertEquals(120, saved.getUsageDuration());
    }

    @Test
    @DisplayName("should throw exception if giver subscriber not found")
    void testProcessMessage_giverNotFound() {
        // Arrange
        when(subscriberRepository.findByPhoneNumber("500")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chargingService.processCGFMessage(message));

        assertTrue(exception.getMessage().contains("Giver subscriber bulunamadı"));
        verify(personalUsageRepository, never()).save(any());
    }
}
