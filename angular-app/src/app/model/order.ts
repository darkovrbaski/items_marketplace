import { Article, emptyArticle } from './article';
import { Money, emptyMoney } from './money';
import { Trade } from './trade';
import { emptyUser, User } from './user';

export interface Order {
  id: number;
  createdDateTime: Date;
  type: OrderType;
  price: Money;
  lowerSellPrice: Money;
  quantity: number;
  filledQuantity: number;
  status: OrderStatus;
  trades: Trade[];
  user: User | null;
  article: Article;
  enabledAutoTrade: boolean;
}

export enum OrderType {
  BUY = 'BUY',
  SELL = 'SELL',
}

export enum OrderStatus {
  OPEN = 'OPEN',
  CLOSED = 'CLOSED',
}

export const emptyLowerPriceMoney: Money = {
  amount: 0,
  currency: 'USD',
};

export const emptyOrder: Order = {
  id: 0,
  createdDateTime: new Date(),
  type: OrderType.BUY,
  price: emptyMoney,
  lowerSellPrice: emptyLowerPriceMoney,
  quantity: 0,
  filledQuantity: 0,
  status: OrderStatus.OPEN,
  trades: [],
  user: emptyUser,
  article: emptyArticle,
  enabledAutoTrade: true,
};
