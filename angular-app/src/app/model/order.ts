import { Article, emptyArticle } from './article';
import { Money, emptyMoney } from './money';
import { Trade } from './trade';
import { emptyUser, User } from './user';

export interface Order {
  id: number;
  createdDateTime: Date;
  type: OrderType;
  price: Money;
  quantity: number;
  filledQuantity: number;
  status: OrderStatus;
  trades: Trade[];
  user: User | null;
  article: Article;
}

export enum OrderType {
  BUY = 'BUY',
  SELL = 'SELL',
}

export enum OrderStatus {
  OPEN = 'OPEN',
  CLOSED = 'CLOSED',
}

export const emptyOrder: Order = {
  id: 0,
  createdDateTime: new Date(),
  type: OrderType.BUY,
  price: emptyMoney,
  quantity: 0,
  filledQuantity: 0,
  status: OrderStatus.OPEN,
  trades: [],
  user: emptyUser,
  article: emptyArticle,
};
