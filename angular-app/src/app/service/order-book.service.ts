import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OrderBook } from '@app/model';
import { environment } from '@environments/environment.development';
import { Observable } from 'rxjs';

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
