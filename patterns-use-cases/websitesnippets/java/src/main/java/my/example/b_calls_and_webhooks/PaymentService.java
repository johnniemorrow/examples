/*
 * Copyright (c) 2024 - Restate Software, Inc., Restate GmbH
 *
 * This file is part of the Restate examples,
 * which is released under the MIT license.
 *
 * You can find a copy of the license in the file LICENSE
 * in the root directory of this repository or package or at
 * https://github.com/restatedev/examples/
 */

package my.example.b_calls_and_webhooks;

import com.stripe.model.PaymentIntent;
import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import my.example.b_calls_and_webhooks.types.PaymentRequest;
import my.example.b_calls_and_webhooks.utils.PaymentUtils;

import java.util.Map;

import static my.example.b_calls_and_webhooks.utils.PaymentUtils.SERDE;
import static my.example.b_calls_and_webhooks.utils.PaymentUtils.verifyPayment;
import static my.example.b_calls_and_webhooks.utils.StripeUtils.submitPayment;
import static my.example.b_calls_and_webhooks.utils.StripeUtils.verifyAndParseEvent;

// *** BEGIN SNIPPET ***

@Service
public class PaymentService {

  @Handler
  public void processPayment(Context ctx, PaymentRequest request) {
    var webhookFuture = ctx.awakeable(SERDE);

    var payment = ctx.run("Stripe call", SERDE, () -> submitPayment(
            request, Map.of("restate_callback_id", webhookFuture.id())
    ));

    if (payment.getStatus().equals("processing")) {
      // synchronous response inconclusive, await webhook response
      var updatedPayment = webhookFuture.await();
      verifyPayment(updatedPayment);
    } else {
      verifyPayment(payment);
    }
  }

  @Handler
  public void processWebhook(Context ctx) {
    var paymentEvent = verifyAndParseEvent(ctx.request());
    String callbackId = paymentEvent.getMetadata().get("restate_callback_id");
    ctx.awakeableHandle(callbackId).resolve(SERDE, paymentEvent);
  }
}

// *** END SNIPPET ***