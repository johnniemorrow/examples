package my.example.g_sagas;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import dev.restate.sdk.common.Serde;
import dev.restate.sdk.common.TerminalException;
import dev.restate.sdk.serde.jackson.JacksonSerdes;
import my.example.g_sagas.types.Product;
import my.example.g_sagas.types.Reservation;

import java.util.ArrayList;
import java.util.List;

import static my.example.g_sagas.utils.Stubs.cancelReservation;
import static my.example.g_sagas.utils.Stubs.reserve;



@Service
public class ReservationService {

    private static final Serde<Reservation> RESERVE_SERDE = JacksonSerdes.of(Reservation.class);

    // *** BEGIN SNIPPET ***

    @Handler
    public void reserveAllProducts(Context ctx, Product[] products) {
        final List<Reservation> reservations = new ArrayList<>();
        try {
            for (Product product : products) {
                Reservation res = ctx.run("Reserve " + product.getId(),
                    RESERVE_SERDE, () -> reserve(product)
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

    // *** END SNIPPET ***
}


