package my.example.f_idempotency;

import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.client.CallRequestOptions;
import my.example.f_idempotency.types.Reservation;
import my.example.f_idempotency.types.ReservationRequest;
import my.example.f_idempotency.utils.ProductServiceClient;

import java.io.IOException;

import static my.example.f_idempotency.utils.Stubs.*;

// *** BEGIN SNIPPET ***

public class IdempotentReservationService {
    public static void main(String[] args) throws IOException {
        HttpServer server = createHttpServer();

        server.createContext("/reserve", httpExchange -> {
            ReservationRequest req = parseToReservationRequest(httpExchange);

            Reservation reservation = ProductServiceClient.fromIngress(RESTATE_RUNTIME_ENDPOINT)
                .reserve(req.getProduct(),
                    CallRequestOptions.DEFAULT.withIdempotency(req.getReservationId()));

            sendResponse(httpExchange, reservation);
        });

        server.start();
    }
}

// *** END SNIPPET ***