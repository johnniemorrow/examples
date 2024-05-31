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
import dev.restate.sdk.Awakeable;
import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import my.example.b_calls_and_webhooks.types.PaymentRequest;
import my.example.b_calls_and_webhooks.utils.PaymentUtils;

import static my.example.b_calls_and_webhooks.utils.PaymentUtils.paymentIntentSerde;
import static my.example.b_calls_and_webhooks.utils.PaymentUtils.verifyPayment;
import static my.example.b_calls_and_webhooks.utils.StripeUtils.createPaymentIntent;
import static my.example.b_calls_and_webhooks.utils.StripeUtils.verifyAndParseEvent;

// *** BEGIN SNIPPET ***

@Service
public class PaymentService {

    @Handler
    public void processPayment(Context ctx, PaymentRequest request) {
        Awakeable<PaymentIntent> webhookPromise = ctx.awakeable(paymentIntentSerde);

        PaymentIntent paymentIntent = ctx.run("Stripe call", paymentIntentSerde,
            () -> createPaymentIntent(request, webhookPromise.id()));

        if (paymentIntent.getStatus().equals("processing")) {
            // synchronous response inconclusive, await webhook response
            PaymentIntent updatedIntent = webhookPromise.await();
            verifyPayment(updatedIntent);
        } else {
            verifyPayment(paymentIntent);
        }
    }

    @Handler
    public void processWebhook(Context ctx) {
        PaymentIntent paymentIntent = verifyAndParseEvent(ctx.request());
        String webhookPromise = paymentIntent.getMetadata().get(PaymentUtils.RESTATE_CALLBACK_ID);
        ctx.awakeableHandle(webhookPromise).resolve(paymentIntentSerde, paymentIntent);
    }
}

// *** END SNIPPET ***