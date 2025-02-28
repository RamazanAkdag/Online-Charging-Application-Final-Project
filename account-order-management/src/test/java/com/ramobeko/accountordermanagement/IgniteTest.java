package com.ramobeko.accountordermanagement;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.*;
import org.apache.ignite.configuration.ClientConfiguration;

public class IgniteTest {
    public static void main(String[] args) {
        ClientConfiguration cfg = new ClientConfiguration()
                .setAddresses("18.158.110.143:10800")
                .setTimeout(15000); // Timeout artırıldı

        IgniteClient client = Ignition.startClient(cfg);
        System.out.println("✅ Ignite Thin Client bağlantısı başarılı!");

    }
}
