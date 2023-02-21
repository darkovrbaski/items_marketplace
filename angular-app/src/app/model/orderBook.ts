import { Money } from './money';

export interface OrderBook {
  sellOrders: AcumulatedOrder[];
  buyOrders: AcumulatedOrder[];
}

export interface AcumulatedOrder {
  price: Money;
  quantity: number;
}

export const emptyOrderBook: OrderBook = {
  sellOrders: [],
  buyOrders: [],
};
