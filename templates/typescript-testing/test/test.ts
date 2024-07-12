import { RestateTestEnvironment } from "./restate_test_environment";
import {ServiceOne, serviceOne} from "../src/service_one";
import * as clients from "@restatedev/restate-sdk-clients";
import {ObjectTwo, objectTwo} from "../src/object_two";

describe("ExampleService", () => {
    let restateTestEnvironment: RestateTestEnvironment;

    // Deploy Restate and the Service endpoint once for all the tests in this suite
    beforeAll(async () => {
        restateTestEnvironment = await RestateTestEnvironment.start(
            (restateServer) =>
                restateServer.bind(serviceOne).bind(objectTwo)
        );
    }, 20_000);

    // Stop Restate and the Service endpoint
    afterAll(async () => {
        if (restateTestEnvironment !== undefined) {
            await restateTestEnvironment.stop();
        }
    });

    it("works", async () => {
        const rs = clients.connect({url: restateTestEnvironment.baseUrl()});
        // Schedule task
        rs.serviceSendClient(ServiceOne)
            .greet("Sarah");

        // Wait until the task and all following subtasks are finished
        // This works for one-way calls but not for delayed calls
        await restateTestEnvironment.waitUntilFinished()

        // Get the state for the nested object call after the call has finished
        const state = await restateTestEnvironment.getKVState(ObjectTwo.name, "Sarah");

        const count = +state.find(e => e.key === "count").value_utf8;
        expect(count).toBe(2);
    });
});