package my.example.d_state_machine.utils;

import dev.restate.sdk.common.StateKey;
import dev.restate.sdk.serde.jackson.JacksonSerdes;
import my.example.c_async_tasks.types.PaymentRequest;
import my.example.d_state_machine.types.Status;

public class Stubs {

    public static final StateKey<Status> STATE_STATUS =
        StateKey.of("status", JacksonSerdes.of(Status.class));
    public static final StateKey<PaymentRequest> STATE_PAYMENT_REQUEST =
        StateKey.of("payment", JacksonSerdes.of(PaymentRequest.class));

    public static void wireFunds(PaymentRequest payment) {
        System.out.println("Wiring funds: " + payment.getAmount());
    }

    public static void refund(PaymentRequest payment) {
        System.out.println("Refunding payment: " + payment.getAmount());
    }
}
