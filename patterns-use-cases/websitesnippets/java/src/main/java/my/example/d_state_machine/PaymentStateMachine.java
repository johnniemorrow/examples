package my.example.d_state_machine;

import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;
import dev.restate.sdk.common.StateKey;
import dev.restate.sdk.common.TerminalException;
import dev.restate.sdk.serde.jackson.JacksonSerdes;
import my.example.c_async_tasks.types.PaymentRequest;
import my.example.d_state_machine.types.Status;

import static my.example.d_state_machine.types.Status.*;
import static my.example.d_state_machine.utils.Stubs.refund;
import static my.example.d_state_machine.utils.Stubs.wireFunds;

// *** BEGIN SNIPPET ***

@VirtualObject
public class PaymentStateMachine {

    StateKey<Status> STATUS =
        StateKey.of("status", JacksonSerdes.of(Status.class));
    StateKey<PaymentRequest> PAYMENT_REQUEST =
            StateKey.of("payment", JacksonSerdes.of(PaymentRequest.class));

    @Handler
    public String makePayment(ObjectContext ctx, PaymentRequest payment) {
        String paymentId = ctx.key();
        switch (ctx.get(STATUS).orElse(NEW)) {
            case CANCELLED -> { return paymentId + " was cancelled before"; }
            case SUCCESS -> { return paymentId + " was previously completed"; }
        }

        wireFunds(payment);
        ctx.set(STATUS, SUCCESS);
        ctx.set(PAYMENT_REQUEST, payment);
        return paymentId + " was successfully processed";
    }

    @Handler
    public void cancelPayment(ObjectContext ctx) {
        Status status = ctx.get(STATUS)
                .orElseThrow(() -> new TerminalException("Payment not found"));
        if (status == SUCCESS) {
            PaymentRequest payment = ctx.get(PAYMENT_REQUEST)
                    .orElseThrow(() -> new TerminalException("Payment not found"));
            refund(payment);
        }
        ctx.set(STATUS, CANCELLED);
    }
}

// *** END SNIPPET ***

