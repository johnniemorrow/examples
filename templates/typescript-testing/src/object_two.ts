import * as restate from "@restatedev/restate-sdk";

export const objectTwo = restate.object({
    name: "ObjectTwo",
    handlers: {
        add: async (ctx: restate.ObjectContext) => {
            console.info("Hello I am object two")

            const count = await ctx.get<number>("count") ?? 0;
            ctx.set("count", count + 1);
            await ctx.sleep(1000);
            ctx.set("count", count + 2);

            return `Hello ${ctx.key}!`;
        }
    },
})

export const ObjectTwo: typeof objectTwo = {name: "ObjectTwo"}