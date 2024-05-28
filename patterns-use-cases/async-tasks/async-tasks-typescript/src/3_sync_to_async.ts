import * as restate from "@restatedev/restate-sdk";
import * as clients from "@restatedev/restate-sdk-clients";
import * as readline from "readline";

const RESTATE_URL = process.env.RESTATE_URL ?? "http://localhost:8080";

//
// DOCS!
//

const dataPreparationService = restate.workflow({
    name: "dataPreparation",
    handlers: {
        run: async (ctx: restate.WorkflowContext, args: { userId: string }): Promise<URL> => {
            const url = await ctx.run("create S3 bucket", () => createS3Bucket());
            await ctx.run("upload data", () => uploadData(url));

            ctx.promise<URL>("url").resolve(url);

            return url;
        },

        resultAsEmail: async (ctx: restate.WorkflowSharedContext, req: { email: string }) => {
            const url = await ctx.promise<URL>("url");
            await ctx.run("send email", () => sendEmail(url, req.email ));
        }
    }
});

export type DataPreparationService = typeof dataPreparationService;

// Client:
//
// The client calls the data preparation workflow and awaits the result for
// a while. If the workflow doesn't complete within that time, it signals the
// workflow to send an email instead. 

async function downloadData(userId: string) {
    const workflowId = userId;

    // connect to the Restate server and create a client for the data preparation workflow 
    const dataPrep = clients.connect({ url: RESTATE_URL })
        .workflowClient<DataPreparationService>({ name: "dataPreparation" }, workflowId);    

    // kick off a new data preparation workflow. this is idempotent per workflow-id
    await dataPrep.workflowSubmit({ userId });

    // wait for the result for 30 secs
    const result = await withTimeout(dataPrep.workflowAttach(), 30_000);

    // if it takes longer, rewire the workflow to send an email instead
    if (result === Timeout) {
        const email = await readLine("This takes longer, give us an email, we'll mail you the link: ");
        await dataPrep.resultAsEmail({ email });
        return;
    }

    // if returns within 30 secs, process directly
}

// ----------------------- Stubs to please the compiler -----------------------

const Timeout = Symbol("Timeout")

function withTimeout<T>(promise: Promise<T>, millis: number): Promise<T | typeof Timeout> {
    const timeoutPromise = new Promise<typeof Timeout>((resolve) => setTimeout(resolve, millis, Timeout));
    return Promise.race([promise, timeoutPromise]);
}

async function readLine(prompt: string): Promise<string> {
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout
    });

    return new Promise<string>(resolve => rl.question(prompt, resolve)).finally(() => rl.close());
}

async function createS3Bucket(): Promise<URL> {
    const bucket = Number(Math.random() * 1_000_000_000).toString(16);
    return new URL(`https://s3-eu-central-1.amazonaws.com/${bucket}/`);
  }
  
  async function uploadData(target: URL) {
      // simulate some work by delaying for a while. sometimes takes really long.
      return new Promise((resolve) => setTimeout(resolve, Math.random() < 0.0 ? 1_500 : 10_000));
  }
  
  async function sendEmail(url: URL, email: string) {
      // send email
      console.log(` >>> Sending email to '${email}' with URL ${url}`);
  }