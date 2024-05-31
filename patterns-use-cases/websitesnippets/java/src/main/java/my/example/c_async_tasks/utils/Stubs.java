package my.example.c_async_tasks.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.client.CallRequestOptions;
import my.example.c_async_tasks.types.PaymentRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;

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
        String request = parseToString(t);
        try {
            return new ObjectMapper().readValue(request, PaymentRequest.class);
        } catch (JsonProcessingException e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String parseToHandle(HttpExchange t){
        return parseToString(t);
    }

    private static String parseToString(HttpExchange t){
        try {
            InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
            BufferedReader br = new BufferedReader(isr);

            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }

            br.close();
            isr.close();
            return buf.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendResponse(HttpExchange t, String response) throws IOException {
        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
