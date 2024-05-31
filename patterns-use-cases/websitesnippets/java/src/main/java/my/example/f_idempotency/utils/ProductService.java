package my.example.f_idempotency.utils;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import my.example.f_idempotency.types.Reservation;

@Service
public class ProductService {

    @Handler
    public Reservation reserve(Context ctx, String productId) {
        // create product
        return new Reservation();
    }
}
