import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Money, StripeResponse, Wallet } from '@app/model';
import { environment } from '@environments/environment.development';
import { Observable } from 'rxjs';

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
