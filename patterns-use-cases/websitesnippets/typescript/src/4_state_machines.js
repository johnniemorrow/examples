import * as restate from "@restatedev/restate-sdk";

// *** BEGIN SNIPPET ***

const paymentSvc = restate.object({
  name: "payments",
  handlers: {
    makePayment: async (ctx, payment) => {
      const paymentId = ctx.key;
      switch (await ctx.get("status")) {
        case "CANCELLED":
            return `${paymentId} was cancelled before`;
        case "SUCCESS":
            return `${paymentId} previously complated`;
      }

      wireFunds(payment);

      ctx.set("status", "SUCCESS");
      ctx.set("payment", payment);
    },

    cancelPayment: async (ctx) => {
      const status = await ctx.get("status");
      if (status === "SUCCESS") {
        const payment = await ctx.get("payment");
        refund(payment);
      }
      ctx.set("status", "CANCELLED");
    }
  }
});

// *** BEGIN SNIPPET ***

// ----------------------- Stubs to please the compiler -----------------------

const EXPIRY_TIMEOUT = 24 * 60 * 60 * 1000;

function wireFunds(payment) {}
function refund(payment) {}