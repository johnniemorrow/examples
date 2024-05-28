import * as restate from "@restatedev/restate-sdk";
import { Context, CombineablePromise } from "@restatedev/restate-sdk";

/*
 * Restate makes it easy to run async work in parallel, fan-out / fan-in style:
 *
 * - every handler is an asynchronous task, invoked through a fast reliably queue
 * - invocations produce durable promises that can be awaited and combined
 * - the durable execution ensures that the fan-out and fan-in steps happen
 *   reliably exactly once.
 *
 * The code below shows how to build a fault-tolerant fan-out task flow.
 * 
 * Deploy this service on an platform like Kubernetes or AWS Lambda to
 * automatically get parallel scale out. 
 */
const workerService = restate.service({
  name: "worker",
  handlers: {
    /**
     * The main work handler that fans out / fans in the subtasks.
     */
    run: async (ctx: Context, task: Task) => {
      // run the first step of the work, creating a set of subtasks
      const subtasks: SubTask[] = await ctx.run("first step", () => process(task));

      // fan out work by calling the subtask handler for each subtask. the subtasks
      // might run in different processes, if this is deployed in a parallel setup 

      // invocation promises recover from failures, re-connect to running subtasks
      const subtaskResults: CombineablePromise<SubTaskResult>[] = [];

      for (const subtask of subtasks) {
        const subResult = ctx.serviceClient(workerService).runSubtask(subtask);
        subtaskResults.push(subResult);
      }

      // fan in by simply awaiting the combined promise
      const results: SubTaskResult[] = await CombineablePromise.all(subtaskResults);
      return aggregate(results);
    },

    /**
     * The handler for the subtask processing. Put this onto a separate service (or
     * serverless function) if you want this to scale independently.
     */
    runSubtask: async (ctx: Context, subtask: SubTask) => {
      // the actual subtask processing
    }
  }
});

export const handler = restate
  .endpoint()
  .bind(workerService)
  .lambdaHandler();

// ----------------------- Stubs to please the compiler -----------------------

type Task = {}
type Result = {}

type SubTask = { }
type SubTaskResult = void

async function process(task: Task): Promise<SubTask[]> {
  return [];
}

async function aggregate(packages: SubTaskResult[]): Promise<Result> {
  return {};
}
