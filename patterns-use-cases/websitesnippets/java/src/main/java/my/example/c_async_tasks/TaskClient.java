package my.example.c_async_tasks;

import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.client.IngressClient;
import dev.restate.sdk.client.SendResponse;
import my.example.c_async_tasks.types.PaymentRequest;
import my.example.c_async_tasks.utils.AsyncTaskServiceClient;

import java.io.IOException;

import static my.example.c_async_tasks.utils.Stubs.*;
import static dev.restate.sdk.JsonSerdes.STRING;

public class TaskClient {
  public static void main(String[] args) throws IOException {
    HttpServer server = createHttpServer();

    // *** BEGIN SNIPPET ***

    // --- start payment task ---
    server.createContext("/charge", httpExchange -> {
      PaymentRequest req = parsePaymentRequest(httpExchange);

      SendResponse handle = AsyncTaskServiceClient
          .fromIngress(RESTATE_URI)
          .send()
          .processPayment(req, idempotencyKey(req.getPaymentId()));

      respondJson(httpExchange, handle);
    });

    //  --- connect to payment result ---
    server.createContext("/status", httpExchange -> {
      String handle = parseToHandle(httpExchange);

      String response = IngressClient.defaultClient(RESTATE_URI)
          .invocationHandle(handle, STRING)
          .attach();
      respond(httpExchange, response);
    });

    // *** END SNIPPET ***

    server.start();
  }
}