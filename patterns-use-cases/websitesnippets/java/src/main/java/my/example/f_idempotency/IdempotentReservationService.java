package my.example.f_idempotency;

import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.client.CallRequestOptions;
import my.example.f_idempotency.types.Reservation;
import my.example.f_idempotency.types.ReservationRequest;
import my.example.f_idempotency.utils.ProductServiceClient;

import java.io.IOException;

import static my.example.f_idempotency.utils.Stubs.*;



public class IdempotentReservationService {
  public static void main(String[] args) throws IOException {

    HttpServer server = createHttpServer();

    // *** BEGIN SNIPPET ***

    server.createContext("/reserve", httpExchange -> {
      ReservationRequest req = parseRequest(httpExchange.getRequestBody());

      // derive an idempotency key from the parameters
      var idempotencyOps = CallRequestOptions.DEFAULT
          .withIdempotency(req.getReservationId());

      // add idempotency opts to the request to let the service automatically
      // fuse repeated requests
      Reservation reservation = ProductServiceClient
          .fromIngress(RESTATE_RUNTIME_ENDPOINT)
          .reserve(req.getProduct(), idempotencyOps);

      sendResponse(httpExchange, reservation);
    });

    // *** END SNIPPET ***

    server.start();
  }
}

