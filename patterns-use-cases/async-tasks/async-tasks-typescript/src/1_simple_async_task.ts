import * as restate from "@restatedev/restate-sdk";
import { connect, SendOpts } from "@restatedev/restate-sdk-clients";

/*
 * Restate is as a sophisticated task queue, with extra features like
 * stateful tasks, queues per key, or workflows-as-code within tasks,
 * and reliable timers.
 * 
 * Every handler in Restate is executed asynchronously and can be treated
 * as a reliable asynchronous task. Restate persists invocations, restarts
 * upon failures, reliably queues them under backpressure.
 */

// --------------- define async task logic as a service handler ---------------

const asyncTaskService = restate.service({
    name: "taskWorker",
    handlers: {
        runTask: async (ctx: restate.Context, params: TaskOpts) => {
            return someHeavyWork(params);
        }
    }
});

export type AsyncTaskService = typeof asyncTaskService;

const endpoint = restate.endpoint().bind(asyncTaskService);
// for containers: endpoint.listen(9080);
// for AWS Lambda: export const handler = endpoint.lambdaHandler();


// ------------------- submit and await tasks like RPC calls ------------------

async function submitAndAwaitTask(task: TaskOpts) {

    const rs = connect({ url: process.env.RESTATE_URL! });
    
    // call a handler asynchronously - this returns once the call in enqueued
    // and yields a handle to re-connect later
    const taskHandle = await rs
        .serviceSendClient<AsyncTaskService>({ name: "taskWorker" })
        .runTask(
            task,
            SendOpts.from({ idempotencyKey: "dQw4w9WgXcQ" }) // avoid double submit
        );

    // await the handler's result 
    const result = await rs.result(taskHandle);

    // this works across processes 
    const serialized = JSON.stringify(taskHandle);
    const rs2 = connect({ url: process.env.RESTATE_URL! });
    const result2 = await rs2.result<string>(JSON.parse(serialized));
}

// ----------------------- Stubs to please the compiler -----------------------

type TaskOpts = {}

function someHeavyWork(work: TaskOpts) { return "Work!"}
