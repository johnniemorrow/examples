import * as restate from "@restatedev/restate-sdk";

// *** BEGIN SNIPPET ***

const paymentSvc = restate.service({
  name: "payments",
  handlers: {
    processPayment: async (ctx, request) => {
      const webhookPromise = ctx.awakeable();

      const paymentIntent = await ctx.run("stripe call", () =>
        createPaymentIntent({
          request,
          metadata: { restate_callback_id: webhookPromise.id }
        })
      );

      if (paymentIntent.status === "processing") {
        // synchronous response inconclusive, await webhook response
        const paymentIntentFromWebhook = await webhookPromise.promise;
        return verifyPayment(paymentIntentFromWebhook);
      } else {
        return verifyPayment(paymentIntent);
      }
    },

    processWebhook: async (ctx) => {
      const paymentIntent = verifyAndParseEvent(ctx.request());
      const webhookPromiseId = paymentIntent.metadata.restate_callback_id;
      ctx.resolveAwakeable(webhookPromiseId, paymentIntent);
    }
  }
});

// *** END SNIPPET ***

// ----------------------- Stubs  -----------------------

import Stripe from "stripe";

const stripeSecretKey = "sk_test_...";
const webHookSecret = "whsec_...";

const stripe = new Stripe(stripeSecretKey, { apiVersion: "2023-10-16" });

function verifyAndParseEvent(req) {
  const requestBody = req.body;
  const signature = req.headers.get("stripe-signature");
  if (!signature) {
    throw new restate.TerminalError("Missing 'stripe-signature' header.", {
      errorCode: 400,
    });
  }
  try {
    return stripe.webhooks.constructEvent(
      requestBody,
      signature,
      webHookSecret
    ).data.object;
  } catch (err) {
    throw new restate.TerminalError(`Webhook Error: ${err}`, {
      errorCode: 400,
    });
  }
}

function verifyPayment(paymentIntent) {}

function createPaymentIntent(request) {
  return stripe.paymentIntents.create({
      amount: request.request.amount,
      currency: "usd",
      payment_method: request.request.paymentMethodId,
      confirm: true,
      confirmation_method: "automatic",
      return_url: "https://restate.dev/", // some random URL
      metadata: { ...request.metadata }
    }
  );
}

