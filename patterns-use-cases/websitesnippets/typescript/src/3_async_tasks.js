const restate = require("@restatedev/restate-sdk");
const clients = require ("@restatedev/restate-sdk-clients");

// *** BEGIN SNIPPET ***

// ------ service (= worker) ------
const asyncTaskService = restate.service({
    name: "taskWorker",
    handlers: { processPayment }
});

// ------ client ------
const rs = clients.connect({ url: process.env.RESTATE_URL });
const taskWorker = rs.serviceSendClient({ name: "taskWorker" });

// submit the payment task 
app.post('/charge/:paymentId', async (req, res) => {
    const taskHandle = await taskWorker.processPayment(
        { request: req.params },
        SendOpts.from({ idempotencyKey: req.params.paymentId })
    );
    
    res.json(taskHandle);
});

// await the payment task
app.get('/status', async (req,res) => {
        const taskHandle = req.body.json();
        const paymentResult = await restate.result(taskHandle); 
        res.join(paymentResult);
});

// *** END SNIPPET ***

// ----------------------- Stubs  -----------------------

async function processPayment(paymentDetails) {}
