/*package org.example.akka;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ramobeko.akka.Command;
import com.ramobeko.akka.CommonServiceKeys;

import java.util.Set;

public class DgwActor extends AbstractBehavior<Object> {

    private final HazelcastInstance hazelcastInstance;
    private ActorRef<Command.UsageData> ocsRouter;

    private DgwActor(ActorContext<Object> context, HazelcastInstance hazelcastInstance) {
        super(context);
        this.hazelcastInstance = hazelcastInstance;

        ActorRef<Receptionist.Listing> listingAdapter =
                context.messageAdapter(Receptionist.Listing.class, WrappedListing::new);
        context.getSystem().receptionist().tell(
                Receptionist.subscribe(CommonServiceKeys.OCS_ROUTER_KEY, listingAdapter)
        );
    }

    public static Behavior<Object> create(HazelcastInstance hazelcastInstance) {
        return Behaviors.setup(context -> new DgwActor(context, hazelcastInstance));
    }

    @Override
    public Receive<Object> createReceive() {
        return newReceiveBuilder()
                .onMessage(Command.UsageData.class, this::processUsageData)
                .onMessage(WrappedListing.class, this::onListing)
                .build();
    }

    private Behavior<Object> onListing(WrappedListing wrappedListing) {
        Receptionist.Listing listing = wrappedListing.listing;
        Set<ActorRef<Command.UsageData>> routers = listing.getServiceInstances(CommonServiceKeys.OCS_ROUTER_KEY);
        if (!routers.isEmpty()) {
            ocsRouter = routers.iterator().next();
            getContext().getLog().info("OCS Router bulundu: {}", ocsRouter);
        } else {
            getContext().getLog().info("OCS Router bulunamadı!");
        }
        return this;
    }

    private Behavior<Object> processUsageData(Command.UsageData data) {
        IMap<String, Long> userCache = hazelcastInstance.getMap("subscriberCache");
        String actorId = getContext().getSelf().path().name();
        getContext().getLog().info("🔥 [{}] Baba merhaba!", actorId);

        if (userCache.containsKey(data.getUserId())) {
            getContext().getLog().info("✅ [{}] Kullanıcı doğrulandı: {}", actorId, data.getUserId());
            getContext().getLog().info("🔹 [{}] Kullanım Türü: {}", actorId, data.getServiceType());
            getContext().getLog().info("🔹 [{}] Kullanım Miktarı: {}", actorId, data.getUsageAmount());

            if (ocsRouter != null) {
                ocsRouter.tell(data);
                getContext().getLog().info("Mesaj OCS Router'a gönderildi.");
            } else {
                getContext().getLog().info("OCS Router bulunamadı, mesaj gönderilemedi!");
            }
        } else {
            getContext().getLog().info("❌ [{}] Kullanıcı bulunamadı: {}", actorId, data.getUserId());
        }
        return this;
    }

    private static final class WrappedListing {
        final Receptionist.Listing listing;
        private WrappedListing(Receptionist.Listing listing) {
            this.listing = listing;
        }
    }
}*/
