import { emptyMoney, Money } from './money';
import { emptyUser, User } from './user';

export interface Wallet {
  balance: Money;
  user: User;
}

export const emptyWallet: Wallet = {
  balance: emptyMoney,
  user: emptyUser,
};
