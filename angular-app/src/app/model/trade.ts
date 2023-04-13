import { emptyMoney, Money } from './money';

export interface Trade {
  id: number;
  createdDateTime: Date;
  price: Money;
  quantity: number;
  sellOrderId: number;
  buyOrderId: number;
}

export const emptyTrade: Trade = {
  id: 0,
  createdDateTime: new Date(),
  price: emptyMoney,
  quantity: 0,
  sellOrderId: 0,
  buyOrderId: 0,
};
