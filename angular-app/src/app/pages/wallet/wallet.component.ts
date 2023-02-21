import { Component } from '@angular/core';
import { Wallet, emptyWallet } from 'src/app/model/wallet';
import { Money, emptyMoney } from 'src/app/model/money';
import { WalletService } from 'src/app/service/wallet.service';

@Component({
  selector: 'app-wallet',
  templateUrl: './wallet.component.html',
  styleUrls: ['./wallet.component.scss'],
})
export class WalletComponent {
  wallet: Wallet = emptyWallet;
  predefinedAmounts = [10, 20, 50, 100];
  moneyToAdd: Money = emptyMoney;
  amountInput = 0;

  constructor(private walletService: WalletService) {}

  addFunds(amount: number) {
    this.moneyToAdd.amount = amount;
    this.walletService.addFunds(this.moneyToAdd).subscribe(wallet => {
      this.wallet = wallet;
    });
  }
}
