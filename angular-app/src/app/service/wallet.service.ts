import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Wallet } from '../model/wallet';
import { Money } from '../model/money';

@Injectable({
  providedIn: 'root',
})
export class WalletService {
  private walletUrl = `${environment.apiUrl}/wallet`;

  constructor(private http: HttpClient) {}

  getWallet(): Observable<Wallet> {
    return this.http.get<Wallet>(`${this.walletUrl}/1`);
  }

  addFunds(amount: Money): Observable<Wallet> {
    return this.http.put<Wallet>(`${this.walletUrl}/1/add`, amount);
  }
}
