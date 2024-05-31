package my.example.g_sagas.utils;

import my.example.g_sagas.types.Product;
import my.example.g_sagas.types.Reservation;

public class Stubs {

    public static Reservation reserve(Product product){
        return new Reservation();
    }

    public static void cancelReservation(Reservation reservation){
    }
}
