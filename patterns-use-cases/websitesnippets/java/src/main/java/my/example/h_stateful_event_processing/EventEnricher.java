package my.example.h_stateful_event_processing;

import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;
import dev.restate.sdk.common.StateKey;

import my.example.h_stateful_event_processing.types.Feature;
import my.example.h_stateful_event_processing.types.User;

import static java.time.Duration.ofMillis;
import static dev.restate.sdk.serde.jackson.JacksonSerdes.of;

import static my.example.h_stateful_event_processing.utils.Stubs.send;

// *** BEGIN SNIPPET ***

@VirtualObject
public class EventEnricher {

    static final StateKey<User> USER = StateKey.of("user", of(User.class));

    @Handler
    public void userEvent(ObjectContext ctx, User event) {
        ctx.set(USER, event);
        // time box 100 ms to collect features before emitting result
        EventEnricherClient.fromContext(ctx, ctx.key())
            .send(ofMillis(100)).emit();
    }

    @Handler
    public void featureEvent(ObjectContext ctx, Feature event) {
        User user = ctx.get(USER).orElse(new User());
        user.addFeature(event);
        ctx.set(USER, user);
    }

    @Handler
    public void emit(ObjectContext ctx) {
        send(ctx.key(), ctx.get(USER));
        ctx.clearAll();
    }
}

// *** END SNIPPET ***
