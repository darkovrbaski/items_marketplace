export interface Money {
  amount: number;
  currency: string;
}

export const emptyMoney: Money = {
  amount: 0,
  currency: 'USD',
};
