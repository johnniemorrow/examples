/*
 * Copyright (c) 2024 - Restate Software, Inc., Restate GmbH
 *
 * This file is part of the Restate examples,
 * which is released under the MIT license.
 *
 * You can find a copy of the license in the file LICENSE
 * in the root directory of this repository or package or at
 * https://github.com/restatedev/examples/
 */

package my.example;

import dev.restate.sdk.Context;
import dev.restate.sdk.annotation.Handler;
import dev.restate.sdk.annotation.Service;
import dev.restate.sdk.http.vertx.RestateHttpEndpointBuilder;
import my.example.a_workflows_as_code.RoleUpdateService;
import my.example.b_calls_and_webhooks.PaymentService;


public class AppMain {
  public static void main(String[] args) {
    RestateHttpEndpointBuilder.builder()
            .bind(new RoleUpdateService())
            .bind(new PaymentService())
            .buildAndListen();
  }
}
