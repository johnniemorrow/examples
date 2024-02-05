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

package dev.restate.sdk.examples.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {

  private final String orderId;
  private final String restaurantId;
  private final Product[] products;
  private final double totalCost;
  private final int deliveryDelay;

  @JsonCreator
  public OrderRequest(
      @JsonProperty("orderId") String orderId,
      @JsonProperty("restaurantId") String restaurantId,
      @JsonProperty("products") Product[] products,
      @JsonProperty("totalCost") double totalCost,
      @JsonProperty("deliveryDelay") int deliveryDelay) {
    this.orderId = orderId;
    this.restaurantId = restaurantId;
    this.products = products;
    this.totalCost = totalCost;
    this.deliveryDelay = deliveryDelay;
  }

  public String getOrderId() {
    return orderId;
  }

  public String getRestaurantId() {
    return restaurantId;
  }

  public double getTotalCost() {
    return totalCost;
  }

  public int getDeliveryDelay() {
    return deliveryDelay;
  }
}
