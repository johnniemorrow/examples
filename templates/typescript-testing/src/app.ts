import * as restate from "@restatedev/restate-sdk";
import {serviceOne} from "./service_one";
import {objectTwo} from "./object_two";

// Template of a Restate service and handler
//
// Have a look at the TS QuickStart to learn how to run this: https://docs.restate.dev/get_started/quickstart?sdk=ts
//

// Create the Restate server to accept requests
restate
    .endpoint()
    .bind(serviceOne)
    .bind(objectTwo)
    .listen(9080);
