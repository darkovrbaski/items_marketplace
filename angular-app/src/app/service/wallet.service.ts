import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Wallet } from '../model/wallet';
import { Money } from '../model/money';
import { StripeResponse } from '../model/stripeResponse';

@Injectable({
  providedIn: 'root',
})
export class WalletService {
  private walletUrl = `${environment.apiUrl}/wallet`;

  constructor(private http: HttpClient) {}

  getWallet(): Observable<Wallet> {
    return this.http.get<Wallet>(`${this.walletUrl}`);
  }

  addFunds(amount: Money): Observable<StripeResponse> {
    return this.http.post<StripeResponse>(`${this.walletUrl}/add`, amount);
  }
}
