package org.example.accountbalancemanagementfunction.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KafkaMessage {
    private Long subscriberId;
    private String usageType; // "VOICE", "SMS", "DATA"
    private int usageAmount; // Harcanan dakika, SMS veya veri miktarı (MB cinsinden)
}

