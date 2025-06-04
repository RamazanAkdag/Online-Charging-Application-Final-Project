package org.example.diametergateway.service.concrete;

import akka.actor.typed.ActorRef;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageRequest;
import org.example.service.concrete.UsageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UsageServiceTest {

    @Mock
    private HazelcastInstance hazelcastInstance;

    @Mock
    private ActorRef<Command.UsageData> router;

    @Mock
    private IMap<String, Long> subscriberMap;

    @InjectMocks
    private UsageService usageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // güvenli mocklama - doReturn().when(...) ile
        doReturn(subscriberMap).when(hazelcastInstance).getMap("subscriberCache");

        // alternatif olarak constructor sonrası atama:
        usageService = new UsageService(hazelcastInstance, router);
    }

    @Test
    void processUsageRequest_shouldSendMessage_whenSubscriberExists() {
        // Arrange
        UsageRequest request = new UsageRequest();
        request.setSenderSubscNumber("905555555555");

        doReturn(true).when(subscriberMap).containsKey("905555555555");

        // Act
        String result = usageService.processUsageRequest(request);

        // Assert
        assertEquals("Usage data message sent to DGW actor", result);
        verify(router, times(1)).tell(any(Command.UsageData.class));
    }

    @Test
    void processUsageRequest_shouldReturnError_whenSubscriberDoesNotExist() {
        // Arrange
        UsageRequest request = new UsageRequest();
        request.setSenderSubscNumber("905555555555");

        doReturn(false).when(subscriberMap).containsKey("905555555555");

        // Act
        String result = usageService.processUsageRequest(request);

        // Assert
        assertEquals("User does not exist", result);
        verify(router, never()).tell(any());
    }
}
