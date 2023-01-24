package me.darkovrbaski.items.marketplace.model;

import java.util.List;
import lombok.Value;

@Value
public class Orderbook {

  List<AccumulatedOrders> sellOrders;

  List<AccumulatedOrders> buyOrders;

}