package my.example.c_async_tasks;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
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

// *** END SNIPPET ***



