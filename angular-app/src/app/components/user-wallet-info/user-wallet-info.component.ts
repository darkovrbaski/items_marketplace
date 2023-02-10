import { Component, Input, OnInit } from '@angular/core';
import { emptyWallet, Wallet } from 'src/app/model/wallet';
import { WalletService } from 'src/app/service/wallet.service';

@Component({
  selector: 'app-user-wallet-info',
  templateUrl: './user-wallet-info.component.html',
  styleUrls: ['./user-wallet-info.component.scss'],
})
export class UserWalletInfoComponent implements OnInit {
  @Input() wallet: Wallet = emptyWallet;

  constructor(private walletService: WalletService) {}

  ngOnInit(): void {
    this.getWallet();
  }

  getWallet() {
    this.walletService.getWallet().subscribe(wallet => {
      this.wallet = wallet;
    });
  }
}
