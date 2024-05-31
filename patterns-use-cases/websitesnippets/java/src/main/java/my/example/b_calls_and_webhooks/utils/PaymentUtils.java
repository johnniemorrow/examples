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

import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.ApiResource;
import dev.restate.sdk.common.Serde;

public class PaymentUtils {

  public static final String RESTATE_CALLBACK_ID = "restate_callback_id";

  public static final Serde<PaymentIntent> SERDE =
          Serde.using(
                  intent -> intent.toJson().getBytes(),
                  bytes -> ApiResource.GSON.fromJson(new String(bytes), PaymentIntent.class));

  public static void verifyPayment(PaymentIntent intent) {}

  public static boolean isPaymentIntent(Event event) {
    return event.getType().startsWith("payment_intent");
  }
}
