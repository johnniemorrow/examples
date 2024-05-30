import { workflow } from "@restatedev/restate-sdk";

// *** BEGIN SNIPPET ***

export default workflow({
  name: "verify",
  handlers: {
    run: async (ctx, { email }) => {
      const secret = ctx.run("generate secret", () => crypto.randomUUID());
      await ctx.run("send email", () => sendEmailWithLink({ email, secret }));
      
      const clickSecret = await ctx.promise("email.clicked");
      return clickSecret == secret;
    },

    click: (ctx, { secret }) => ctx.promise("email.clicked").resolve(secret),
  },
});

// *** BEGIN SNIPPET ***



// ------------ longer snippet ---------------------------------------------
// export default workflow({
//   name: "verify",
//   handlers: {
//     run: async (ctx, { email, secret }) => {

//       await ctx.run("send email", () => {
//         const callback = `https://acme.restate.cloud/verify/${ctx.key}/click`;
//         const form = ```
//             <form action=${callback} method="POST>
//               <input type="hidden" name="secret" value="${secret}" />
//               <input type="submit" />
//             </form>
//           ```;

//         return sendEmailWithLink({ email, form });
//       });

//       const clickSecret = await ctx.promise("email.clicked");
//       if (clickSecret == secret) {
//         return "ok";
//       } else {
//         return "mismatch";
//       }
//     },

//     click: (ctx, { secret }) => ctx.promise("email.clicked").resolve(secret),
//   },
// });
// ------------ longer snippet ---------------------------------------------