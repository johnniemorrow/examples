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
package my.example.c_async_tasks.types;

public class PaymentRequest {
  private final String paymentId;
  private final Long amount;
  private final String paymentMethodId;
  private final boolean delayedStatus;

  public PaymentRequest(String paymentId, Long amount, String paymentMethodId, boolean delayedStatus) {
    this.paymentId = paymentId;
    this.amount = amount;
    this.paymentMethodId = paymentMethodId;
    this.delayedStatus = delayedStatus;
  }

  public String getPaymentId() {
    return paymentId;
  }

  public Long getAmount() {
    return amount;
  }

  public String getPaymentMethodId() {
    return paymentMethodId;
  }

  public boolean isDelayed() {
    return delayedStatus;
  }
}
