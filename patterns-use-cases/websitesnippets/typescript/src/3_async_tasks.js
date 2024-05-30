const restate = require("@restatedev/restate-sdk");
const clients = require ("@restatedev/restate-sdk-clients");

// *** BEGIN SNIPPET ***

// --- service (= worker) ---
const asyncTaskService = restate.service({
    name: "taskWorker",
    handlers: {
        runTask: async (ctx, params) => {
            return someHeavyWork(params);
        }
    }
});

// --- client ---
const rs = clients.connect({ url: process.env.RESTATE_URL });
const taskHandle = await rs
    .serviceSendClient({ name: "taskWorker" })
    .runTask(
        { /* task params */ },
        SendOpts.from({ idempotencyKey: "dQw4w9WgXcQ" })
    );

const result = await rs.result(taskHandle);

// *** END SNIPPET ***
