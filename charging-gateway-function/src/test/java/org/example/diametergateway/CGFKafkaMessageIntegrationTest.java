package org.example.charginggatewayfunction;

import com.ramobeko.dgwtgf.model.UsageType;
import com.ramobeko.kafka.message.CGFKafkaMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Date;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = { "cgf_topic" },
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:0", // 9092 yerine 0 (ephemeral port)
                "port=0"
        }
)


class CGFKafkaMessageIntegrationTest {

    @Autowired
    private KafkaTemplate<String, CGFKafkaMessage> kafkaTemplate;

    @Test
    void testSendCGFKafkaMessage() {
        // Test sırasında göndereceğimiz örnek CGWKafkaMessage
        CGFKafkaMessage testMessage = new CGFKafkaMessage(
                UsageType.SMS,          // usageType (enum)
                5.0,                    // usageAmount
                "905551234567",         // senderSubscNumber
                "905558765432",         // receiverSubscNumber
                new Date()              // usageTime
        );

        // Kafka'ya mesajı gönderiyoruz
        kafkaTemplate.send("cgf_topic", testMessage);

        // Konsol/log'da görmek için basit bir çıktı
        System.out.println("➡️  Test mesajı gönderildi: " + testMessage);

        // İsteğe bağlı: Bir süre bekleyip veritabanını veya logları kontrol edebilirsiniz.
        // Örneğin, veritabanında PersonalUsage kaydı oluşmuş mu diye bakabilirsiniz.
        // Bu kısımda genellikle CountDownLatch, Awaitility gibi araçlarla listener'ın cevabını bekleriz.
    }
}

