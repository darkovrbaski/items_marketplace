package me.darkovrbaski.items.marketplace.model;

import java.util.List;

public record OrderBook(

    List<AccumulatedOrder> sellOrders,

    List<AccumulatedOrder> buyOrders

) {

}