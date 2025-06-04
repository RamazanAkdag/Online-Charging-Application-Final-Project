package org.example.service.abstrct;

import java.util.List;

public interface ISubscriberService {
    void printSubscribers();

    List<String> getAllSubscriberNumbers(); // Tüm abonelerin telefon numaralarını döndüren yeni metot
}


