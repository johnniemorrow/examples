package my.example.h_stateful_event_processing;

import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;
import dev.restate.sdk.common.StateKey;
import dev.restate.sdk.serde.jackson.JacksonSerdes;
import my.example.h_stateful_event_processing.types.Feature;
import my.example.h_stateful_event_processing.types.User;

import static my.example.h_stateful_event_processing.utils.Stubs.send;

// *** BEGIN SNIPPET ***

@VirtualObject
public class EventEnricher {

    StateKey<User> USER =
        StateKey.of("user", JacksonSerdes.of(User.class));

    @Handler
    public void userEvent(ObjectContext ctx, User event) {
        // remember event, time box 100 ms to collect features
        // before emitting result
        ctx.set(USER, event);
        EventEnricherClient.fromContext(ctx, ctx.key()).emit();
    }

    @Handler
    public void featureEvent(ObjectContext ctx, Feature event) {
        User user = ctx.get(USER).orElse(new User());
        user.setFeature(event);
        ctx.set(USER, user);
    }

    @Handler
    public void emit(ObjectContext ctx) {
        send(ctx.key(), ctx.get(USER));
        ctx.clearAll();
    }
}

// *** END SNIPPET ***
