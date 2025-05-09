package com.ramobeko.accountordermanagement.service.concrete.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HazelcastSubscriberServiceTest {

    private HazelcastInstance hazelcastInstance;
    private IMap<String, Long> cache;
    private HazelcastSubscriberService hazelcastSubscriberService;

    private final String cacheName = "testCache";

    @BeforeEach
    void setUp() throws Exception {
        hazelcastInstance = mock(HazelcastInstance.class);
        cache = mock(IMap.class); // raw type bile olsa doReturn ile sorun çıkmaz

        hazelcastSubscriberService = new HazelcastSubscriberService(hazelcastInstance);

        // cacheName inject (çünkü @Value Spring olmadan set edilmez)
        Field field = HazelcastSubscriberService.class.getDeclaredField("cacheName");
        field.setAccessible(true);
        field.set(hazelcastSubscriberService, cacheName);

        // thenReturn yerine doReturn kullanıyoruz
        doReturn(cache).when(hazelcastInstance).getMap(cacheName);
    }


    @Test
    void save_shouldPutValueInCache() {
        String phoneNumber = "905551234567";
        Long subscriberId = 123L;

        hazelcastSubscriberService.save(phoneNumber, subscriberId);

        verify(cache, times(1)).put(phoneNumber, subscriberId);
    }

    @Test
    void get_shouldReturnSubscriberId_whenExists() {
        String phoneNumber = "905551234567";
        Long subscriberId = 123L;

        when(cache.get(phoneNumber)).thenReturn(subscriberId);

        Optional<Long> result = hazelcastSubscriberService.get(phoneNumber);

        assertTrue(result.isPresent());
        assertEquals(subscriberId, result.get());
    }

    @Test
    void get_shouldReturnEmpty_whenNotExists() {
        String phoneNumber = "905551234567";

        when(cache.get(phoneNumber)).thenReturn(null);

        Optional<Long> result = hazelcastSubscriberService.get(phoneNumber);

        assertFalse(result.isPresent());
    }

    @Test
    void remove_shouldRemoveValueFromCache() {
        String phoneNumber = "905551234567";

        hazelcastSubscriberService.remove(phoneNumber);

        verify(cache, times(1)).remove(phoneNumber);
    }

    @Test
    void containsKey_shouldReturnTrue_whenKeyExists() {
        String phoneNumber = "905551234567";

        when(cache.containsKey(phoneNumber)).thenReturn(true);

        assertTrue(hazelcastSubscriberService.containsKey(phoneNumber));
    }

    @Test
    void containsKey_shouldReturnFalse_whenKeyDoesNotExist() {
        String phoneNumber = "905551234567";

        when(cache.containsKey(phoneNumber)).thenReturn(false);

        assertFalse(hazelcastSubscriberService.containsKey(phoneNumber));
    }
}
