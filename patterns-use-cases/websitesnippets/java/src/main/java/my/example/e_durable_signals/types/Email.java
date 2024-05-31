package my.example.e_durable_signals.types;

public class Email {

    private final String msg;
    private final String to;
    private final String from;

    public Email(String msg, String to, String from) {
        this.msg = msg;
        this.to = to;
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }
}
