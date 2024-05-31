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

import dev.restate.sdk.http.vertx.RestateHttpEndpointBuilder;
import my.example.a_workflows_as_code.RoleUpdateService;
import my.example.b_calls_and_webhooks.PaymentService;
import my.example.c_async_tasks.AsyncTaskService;
import my.example.d_state_machine.PaymentStateMachine;
import my.example.e_durable_signals.SecretVerifier;
import my.example.f_idempotency.IdempotentReservationService;
import my.example.g_sagas.ReservationService;
import my.example.h_stateful_event_processing.EventEnricher;


public class AppMain {
  public static void main(String[] args) {
    RestateHttpEndpointBuilder.builder()
            .bind(new RoleUpdateService())
            .bind(new PaymentService())
            .bind(new AsyncTaskService())
            .bind(new PaymentStateMachine())
            .bind(new SecretVerifier())
            .bind(new ReservationService())
            .bind(new EventEnricher())
            .buildAndListen();
  }
}
