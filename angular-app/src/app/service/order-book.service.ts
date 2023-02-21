import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { OrderBook } from '../model/orderBook';

@Injectable({
  providedIn: 'root',
})
export class OrderBookService {
  private orderBookUrl = `${environment.apiUrl}/orderBook`;

  constructor(private http: HttpClient) {}

  getOrderBook(articleId: number): Observable<OrderBook> {
    return this.http.get<OrderBook>(`${this.orderBookUrl}/${articleId}`);
  }
}
