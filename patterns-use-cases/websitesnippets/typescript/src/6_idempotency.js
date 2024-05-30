import * as restate from "@restatedev/restate-sdk-clients";
import { Opts } from "@restatedev/restate-sdk-clients";
import express from 'express';

const app = express()

// *** BEGIN SNIPPET ***

const rs = restate.connect({ url: process.env.RESTATE_URL });

app.get('/reserve/:product/:reservationId', async (req, res) => {
    const { product, reservationId } = req.params;

   const products = rs.serviceClient(ProductService);
   const reservation = await products.reserve(
     product,
     Opts.from({ idempotencyKey : reservationId })
   );

   res.json(reservation);
})

// *** END SNIPPET ***