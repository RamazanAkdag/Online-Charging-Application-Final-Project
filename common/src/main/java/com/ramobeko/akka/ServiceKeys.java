package com.ramobeko.akka;

import akka.actor.typed.receptionist.ServiceKey;

/**
 * Ortak kullanılacak ServiceKey'lerin tanımlandığı sınıf.
 */
public final class ServiceKeys {

    // OCS tarafındaki Router aktörü için
    public static final ServiceKey<Command.UsageData> OCS_ROUTER_KEY =
            ServiceKey.create(Command.UsageData.class, "OcsRouter");

    // DGW aktörü için (Command bazında)
    public static final ServiceKey<Command> DGW_ACTOR_KEY =
            ServiceKey.create(Command.class, "DgwActor");

    private ServiceKeys() {} // Nesne oluşturulmasın
}
