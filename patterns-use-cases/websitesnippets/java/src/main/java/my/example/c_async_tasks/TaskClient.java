package my.example.c_async_tasks;

import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.JsonSerdes;
import dev.restate.sdk.client.IngressClient;
import my.example.c_async_tasks.types.PaymentRequest;

import java.io.IOException;

import static my.example.c_async_tasks.utils.Stubs.*;

// *** BEGIN SNIPPET ***

// ------ client ------
public class TaskClient {
    public static void main(String[] args) throws IOException {
        HttpServer server = createHttpServer();

        // submit the payment task
        server.createContext("/charge", httpExchange -> {
            PaymentRequest req = parseToPaymentRequest(httpExchange);

            String handle = AsyncTaskServiceClient.fromIngress(RESTATE_RUNTIME_ENDPOINT)
                    .send()
                    .processPayment(req, idempotencyKey(req.getPaymentId()));

            sendResponse(httpExchange, handle);
        });

        // await the payment task
        server.createContext("/status", httpExchange -> {
            String handle = parseToHandle(httpExchange);

            System.out.println("Awaiting payment task: " + handle);
            String response = IngressClient.defaultClient(RESTATE_RUNTIME_ENDPOINT)
                    .invocationHandle(handle, JsonSerdes.STRING)
                    .attach();
            System.out.println("Payment task completed: " + response);
            sendResponse(httpExchange, response);
        });

        server.start();
    }
}

// *** END SNIPPET ***