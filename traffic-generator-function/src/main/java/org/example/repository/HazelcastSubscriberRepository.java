package org.example;

import com.hazelcast.map.IMap;
import java.util.Map;

public class HazelcastSubscriberRepository implements SubscriberRepository {
    private final IMap<String, Long> subscriberMap;

    public HazelcastSubscriberRepository(IMap<String, Long> subscriberMap) {
        this.subscriberMap = subscriberMap;
    }

    @Override
    public Map<String, Long> getSubscribers() {
        return subscriberMap;
    }

    @Override
    public boolean isEmpty() {
        return subscriberMap.isEmpty();
    }
}
