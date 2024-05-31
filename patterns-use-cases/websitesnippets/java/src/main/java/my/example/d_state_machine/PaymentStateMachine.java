package my.example.d_state_machine;

import dev.restate.sdk.ObjectContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.VirtualObject;
import dev.restate.sdk.common.TerminalException;
import my.example.c_async_tasks.types.PaymentRequest;
import my.example.d_state_machine.types.Status;

import static my.example.d_state_machine.types.Status.*;
import static my.example.d_state_machine.utils.Stubs.*;

// *** BEGIN SNIPPET ***

@VirtualObject
public class PaymentStateMachine {

  @Handler
  public String makePayment(ObjectContext ctx, PaymentRequest payment) {
    String paymentId = ctx.key();

    switch (ctx.get(STATE_STATUS).orElse(NEW)) {
      case CANCELLED: return paymentId + " was cancelled before";
      case SUCCESS:   return paymentId + " was previously completed";
    }

    wireFunds(payment);

    ctx.set(STATE_STATUS, SUCCESS);
    ctx.set(STATE_PAYMENT_REQUEST, payment);
    return paymentId + " was successfully processed";
  }

  @Handler
  public void cancelPayment(ObjectContext ctx) {
    Status status = ctx.get(STATE_STATUS).orElse(NEW);
    if (status == SUCCESS) {
      PaymentRequest payment = ctx.get(STATE_PAYMENT_REQUEST).get();
      refund(payment);
    }
    ctx.set(STATE_STATUS, CANCELLED);
  }
}

// *** END SNIPPET ***

