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
package my.example.b_calls_and_webhooks.utils;

import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentCreateParams.ConfirmationMethod;
import dev.restate.sdk.common.Request;
import dev.restate.sdk.common.TerminalException;
import my.example.b_calls_and_webhooks.types.PaymentRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class StripeUtils {

  Logger logger = LogManager.getLogger(StripeUtils.class);
  private static final String stripeSecretKey = "sk_test_...";
  private static final String webhookSecret = "whsec_...";
  private static final StripeClient stripe = StripeClient.builder().setApiKey(stripeSecretKey).build();

  public static PaymentIntent submitPayment(PaymentRequest request, Map<String, String> metadata) {

    try {
      PaymentIntent paymentIntent =
          stripe
              .paymentIntents()
              .create(
                  new PaymentIntentCreateParams.Builder()
                      .setPaymentMethod(request.getPaymentMethodId())
                      .setAmount(request.getAmount())
                      .setCurrency("USD")
                      .setConfirm(true)
                      .setConfirmationMethod(ConfirmationMethod.AUTOMATIC)
                      .setReturnUrl("https://restate.dev/")
                      .putAllMetadata(metadata)
                      .build());

      if (request.isDelayed()) {
        paymentIntent.setStatus("processing");
      }

      return paymentIntent;
    } catch (StripeException err) {
      // Simulate delayed notifications for testing
      try {
        PaymentIntent paymentIntent = err.getStripeError().getPaymentIntent();
        if (request.isDelayed()) {
          paymentIntent.setStatus("processing");
          return paymentIntent;
        } else {
          throw new TerminalException(
              "Payment declined: " + paymentIntent.getStatus() + " - " + err.getMessage());
        }
      } catch (NullPointerException exc) {
        throw new TerminalException("Payment error: " + exc.getMessage());
      }
    }
  }

  public static PaymentIntent verifyAndParseEvent(Request request) {
    Event event;
    try {
      event = Webhook.constructEvent(new String(request.body()), request.headers().get("stripe-signature"), webhookSecret);
    } catch (SignatureVerificationException e) {
      throw new TerminalException(400, "Invalid Stripe signature");
    }

    return parseAsPaymentIntent(event);
  }

  private static PaymentIntent parseAsPaymentIntent(Event event) {
    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
    PaymentIntent paymentIntent = null;
    if (dataObjectDeserializer.getObject().isPresent()) {
      paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
    } else {
      throw new TerminalException(500, "No Stripe object found in event");
    }

    return paymentIntent;
  }
}
