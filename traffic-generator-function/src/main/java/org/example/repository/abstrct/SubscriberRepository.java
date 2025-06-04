package org.example.repository.abstrct;

import java.util.Map;

public interface SubscriberRepository {
    Map<String, Long> getSubscribers();
    boolean isEmpty();
}

