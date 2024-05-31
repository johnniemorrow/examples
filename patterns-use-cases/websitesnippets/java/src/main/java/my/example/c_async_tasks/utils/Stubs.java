package my.example.c_async_tasks.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dev.restate.sdk.client.CallRequestOptions;
import dev.restate.sdk.client.SendResponse;
import my.example.c_async_tasks.types.PaymentRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Stubs {

    public static final String RESTATE_URI = "http://localhost:8080";

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

    public static PaymentRequest parsePaymentRequest(HttpExchange t){
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

    public static void respondJson(HttpExchange t, SendResponse response) throws IOException {
        throw new Error("unimplemented");
    }

    public static void respond(HttpExchange t, String response) throws IOException {
        final byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
