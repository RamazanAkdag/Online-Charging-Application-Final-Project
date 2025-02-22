package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.hazelcast.core.HazelcastInstance;

public class Main {
    public static void main(String[] args) {
        // Akka Sistemi Başlat
        final ActorSystem system = ActorSystem.create("dgw-system");

        // Hazelcast Client Bağlantısı
        HazelcastInstance hazelcastInstance = HazelcastConfig.createHazelcastClient();

        // 4 Farklı DGW Aktörü Başlat
        final ActorRef dgwRouter1 = system.actorOf(Props.create(DgwActor.class, hazelcastInstance), "dgwRouter1");
        final ActorRef dgwRouter2 = system.actorOf(Props.create(DgwActor.class, hazelcastInstance), "dgwRouter2");
        final ActorRef dgwRouter3 = system.actorOf(Props.create(DgwActor.class, hazelcastInstance), "dgwRouter3");
        final ActorRef dgwRouter4 = system.actorOf(Props.create(DgwActor.class, hazelcastInstance), "dgwRouter4");

        // HTTP Sunucusunu Başlat
        new DgwHttpServer(system, dgwRouter1).startHttpServer();
    }
}
