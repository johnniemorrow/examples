import * as restate from "@restatedev/restate-sdk";
import { emit } from "process";

// *** BEGIN SNIPPET ***

const eventEnricher = restate.object({
  name: "eventEnricher",
  handlers: {
    userEvent: async (ctx, event) => {
      // remember event, wait for 100 ms to collect context, then emit
      ctx.set("user", event);
      ctx.serviceSendClient(eventJoiner, { delay: 100 }).emit();
    },

    featureEvent: async (ctx, featureEvent) => {
      // merge feature into event
      const userEvent = (await ctx.get("user")) ?? {};
      (userEvent.features ??= []).push(featureEvent);
      ctx.set("user", userEvent)
    },

    emit: async (ctx) => {
      emit(ctx.key, await ctx.get("user"));
      ctx.clearAll();
    }
  }
})

// *** END SNIPPET ***
