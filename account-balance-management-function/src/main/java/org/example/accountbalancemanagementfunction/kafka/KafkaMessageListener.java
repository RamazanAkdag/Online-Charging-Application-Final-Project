package org.example.accountbalancemanagementfunction.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.accountbalancemanagementfunction.model.KafkaMessage;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
public class KafkaMessageListener implements MessageListener<String, KafkaMessage> {

    @Override
    public void onMessage(ConsumerRecord<String, KafkaMessage> data) {
        // Kafka'dan gelen mesajı işleyin
        KafkaMessage message = data.value();
        System.out.println("Mesaj alındı: " + message);

        // Burada gelen KafkaMessage nesnesiyle işleme yapabilirsiniz.
    }
}
