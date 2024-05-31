package my.example.f_idempotency.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import my.example.f_idempotency.types.Reservation;
import my.example.f_idempotency.types.ReservationRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Stubs {

    public static final String RESTATE_RUNTIME_ENDPOINT = "http://localhost:8080";

    public static HttpServer createHttpServer(){
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return server;
    }

    public static ReservationRequest parseRequest(InputStream in){
        return new ReservationRequest("id-123", "pm_card_visa");
    }

    public static void sendResponse(HttpExchange t, Reservation reservation) throws IOException {
        t.sendResponseHeaders(200, reservation.toString().length());
        OutputStream os = t.getResponseBody();
        os.write(reservation.toString().getBytes());
        os.close();
    }
}
