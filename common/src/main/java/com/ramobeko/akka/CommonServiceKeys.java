package com.ramobeko.akka;

import akka.actor.typed.receptionist.ServiceKey;
import com.ramobeko.akka.Command;

public class CommonServiceKeys {
    public static final ServiceKey<Command.UsageData> OCS_SERVICE_KEY =
            ServiceKey.create(Command.UsageData.class, "OcsService");
}
