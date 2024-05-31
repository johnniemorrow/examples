package my.example.c_async_tasks.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.client.CallRequestOptions;
import my.example.c_async_tasks.types.PaymentRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Stubs {

    public static final String RESTATE_RUNTIME_ENDPOINT = "http://localhost:8080";

    public static CallRequestOptions idempotencyKey(String idempotencyKey) {
        return CallRequestOptions.DEFAULT.withIdempotency(idempotencyKey);
    }

    public static HttpServer createHttpServer(){
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }
    public static String handlePayment(PaymentRequest req){
        return "Payment processed";
    }

    public static PaymentRequest parseToPaymentRequest(HttpExchange t){
        return new PaymentRequest("id-123", 100L, "pm_card_visa", false);
    }

    public static String parseToHandle(HttpExchange t){
        return "handle-123";
    }

    public static void sendResponse(HttpExchange t, String response) throws IOException {
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
