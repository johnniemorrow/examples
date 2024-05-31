package my.example.d_state_machine.utils;

import my.example.c_async_tasks.types.PaymentRequest;

public class Stubs {

    public static void wireFunds(PaymentRequest payment) {
        System.out.println("Wiring funds: " + payment.getAmount());
    }

    public static void refund(PaymentRequest payment) {
        System.out.println("Refunding payment: " + payment.getAmount());
    }
}
