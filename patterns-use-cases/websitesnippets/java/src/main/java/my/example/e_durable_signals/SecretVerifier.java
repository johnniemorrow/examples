package my.example.e_durable_signals;

import dev.restate.sdk.JsonSerdes;
import dev.restate.sdk.SharedWorkflowContext;
import dev.restate.sdk.WorkflowContext;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Workflow;
import dev.restate.sdk.common.DurablePromiseKey;
import my.example.e_durable_signals.types.Email;

import static my.example.e_durable_signals.utils.Stubs.sendEmailWithLink;

// *** BEGIN SNIPPET ***

@Workflow
public class SecretVerifier {

    DurablePromiseKey<String> EMAIL_CLICKED =
            DurablePromiseKey.of("email_clicked", JsonSerdes.STRING);

    @Workflow
    public boolean run(WorkflowContext ctx, Email email) {
        String secret = ctx.random().nextUUID().toString();
        ctx.run("send email",
            () -> sendEmailWithLink(email, secret));

        String clickSecret = ctx.durablePromise(EMAIL_CLICKED).awaitable().await();
        return clickSecret.equals(secret);
    }

    @Handler
    public void click(SharedWorkflowContext ctx, String secret) {
        ctx.durablePromiseHandle(EMAIL_CLICKED).resolve(secret);
    }
}

// *** END SNIPPET ***