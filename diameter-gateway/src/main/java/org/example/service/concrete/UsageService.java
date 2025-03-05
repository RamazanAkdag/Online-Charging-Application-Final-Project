package org.example.service.concrete;

import akka.actor.typed.ActorRef;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;
import com.ramobeko.dgwtgf.model.UsageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.abstrct.IUsageService;
import org.example.util.mapper.UsageRequestMapper;
import org.springframework.stereotype.Service;

@Service
public class UsageService implements IUsageService {

    private static final Logger logger = LogManager.getLogger(UsageService.class);
    private final HazelcastInstance hazelcastInstance;
    private final ActorRef<Command.UsageData> router;
    private final IMap<String, Long> subscriberMap;

    public UsageService(HazelcastInstance hazelcastInstance, ActorRef<Command.UsageData> router) {
        this.hazelcastInstance = hazelcastInstance;
        this.router = router;
        // İsim konfigürasyondan alınabilir, burada örnek olarak "subscriberCache" kullanıldı.
        this.subscriberMap = hazelcastInstance.getMap("subscriberCache");
    }

    public String processUsageRequest(UsageRequest usageRequest) {
        String senderPhoneNumber = usageRequest.getSenderSubscNumber();
        logger.info("Checking if user with phone number {} exists in Hazelcast subscriber cache...", senderPhoneNumber);

        if (!subscriberMap.containsKey(senderPhoneNumber)) {
            logger.error("User with phone number {} does not exist.", senderPhoneNumber);
            return "User does not exist";
        }

        // Mapper kullanılarak dönüşüm yapılır
        Command.UsageData usageData = UsageRequestMapper.mapToUsageData(usageRequest);
        logger.info("Sending message to router: {}", usageData);
        router.tell(usageData);

        return "Usage data message sent to DGW actor";
    }
}
