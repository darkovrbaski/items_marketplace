import { emptyUser, User } from './user';

export interface Money {
  amount: number;
  currency: string;
}

export interface Wallet {
  balance: Money;
  user: User;
}

export const emptyMoney: Money = {
  amount: 0,
  currency: 'USD',
};

export const emptyWallet: Wallet = {
  balance: emptyMoney,
  user: emptyUser,
};
