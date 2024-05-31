package my.example.g_sagas;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import dev.restate.sdk.common.TerminalException;
import dev.restate.sdk.serde.jackson.JacksonSerdes;
import my.example.g_sagas.types.Product;
import my.example.g_sagas.types.Reservation;

import java.util.ArrayDeque;
import java.util.Deque;

import static my.example.g_sagas.utils.Stubs.cancelReservation;
import static my.example.g_sagas.utils.Stubs.reserve;

// *** BEGIN SNIPPET ***

@Service
public class ReservationService {

    @Handler
    public void processPayment(Context ctx, Product[] products) {

        final Deque<Reservation> reservations = new ArrayDeque<>();
        try {
            for (Product product : products) {
                Reservation res = ctx.run("Product reservation " + product.getId(),
                    JacksonSerdes.of(Reservation.class),
                    () -> reserve(product)
                );
                reservations.add(res);
            }
        } catch (TerminalException e) {
            reservations.forEach(res -> {
                ctx.run("Undo reservation", () -> cancelReservation(res));
            });
            throw e;
        }
    }
}

// *** END SNIPPET ***
