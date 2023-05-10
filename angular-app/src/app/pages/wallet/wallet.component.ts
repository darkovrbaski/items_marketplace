import { Component } from '@angular/core';
import { emptyMoney, emptyWallet, Money, Wallet } from '@app/model';
import { WalletService } from '@app/service';
import { environment } from '@environments/environment.development';
import { loadStripe } from '@stripe/stripe-js/pure';
import { ToastrService } from 'ngx-toastr';

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
