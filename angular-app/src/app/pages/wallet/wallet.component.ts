import { Component } from '@angular/core';
import { Wallet, emptyWallet } from 'src/app/model/wallet';
import { Money, emptyMoney } from 'src/app/model/money';
import { WalletService } from 'src/app/service/wallet.service';
import { ToastrService } from 'ngx-toastr';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from 'src/environments/environment.development';

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
  stripePromise = loadStripe(environment.stripe);

  constructor(
    private walletService: WalletService,
    private toastr: ToastrService
  ) {}

  async addFunds(amount: number) {
    const stripe = await this.stripePromise;
    this.moneyToAdd.amount = amount;

    this.walletService.addFunds(this.moneyToAdd).subscribe({
      next: session => {
        stripe?.redirectToCheckout({ sessionId: session.sessionId });
      },
      error: error => {
        let errors = '';
        error.error.errors.forEach((message: string) => {
          errors += `${message}</br>`;
        });
        this.toastr.error(errors, error.error.message, {
          enableHtml: true,
        });
      },
    });
  }
}
