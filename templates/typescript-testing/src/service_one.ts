import * as restate from "@restatedev/restate-sdk";
import {ObjectTwo} from "./object_two";

export const serviceOne = restate.service({
    name: "ServiceOne",
    handlers: {
        greet: async (ctx: restate.Context, name: string) => {
            console.info("Hello I am service one")

            ctx.objectSendClient(ObjectTwo, name).add();

            return `Hello ${name}!`;
        }
    },
})

export const ServiceOne: typeof serviceOne = {name: "ServiceOne"}