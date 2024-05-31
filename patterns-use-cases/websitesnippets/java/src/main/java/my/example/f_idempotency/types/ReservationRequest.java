package my.example.f_idempotency.types;

public class ReservationRequest {

        private final String product
                ;
        private final String reservationId;

        public ReservationRequest(String product, String reservationId) {
            this.product = product;
            this.reservationId = reservationId;
        }

        public String getProduct() {
            return product;
        }

        public String getReservationId() {
            return reservationId;
        }
}
