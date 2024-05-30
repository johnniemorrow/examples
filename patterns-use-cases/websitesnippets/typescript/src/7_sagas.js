import { Context, TerminalError } from "@restatedev/restate-sdk";

// *** BEGIN SNIPPET ***

async function reservation(ctx, products) {
  const reservations = [];
  try {
    for (const product of products) {
      const reservation = await ctx.run(
        `Product reservation ${product}`, () =>
        reserve(product)
      );
      reservations.push(reservation);
    }
  } catch (error) {
    if (error instanceof TerminalError) {
      for (const reservation of reservations) {
        await ctx.run("undo reservation", () => 
            cancelReservation(reservation)
        );
      }
    }
    throw error;
  }
}

// *** END SNIPPET ***

// ----------------------- Stubs to please the compiler -----------------------

function reserve(product) {
    throw new Error("stub");
}

function cancelReservation(reservation) {
    throw new Error("stub");
}
