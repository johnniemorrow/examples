package my.example.c_async_tasks;

import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.Context;
import dev.restate.sdk.JsonSerdes;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import dev.restate.sdk.client.IngressClient;
import my.example.c_async_tasks.types.PaymentRequest;

import java.io.IOException;

import static my.example.c_async_tasks.utils.Stubs.*;

// *** BEGIN SNIPPET ***

// ------ service (= worker) ------
@Service
public class AsyncTaskService {
    @Handler
    public String processPayment(Context ctx, PaymentRequest req) {
        return handlePayment(req);
    }
}

// ------ client ------
class TaskClient {
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
            String response = IngressClient.defaultClient(RESTATE_RUNTIME_ENDPOINT)
                .invocationHandle(handle, JsonSerdes.STRING)
                .attach();
            sendResponse(httpExchange, response);
        });

        server.start();
    }
}

// *** END SNIPPET ***



