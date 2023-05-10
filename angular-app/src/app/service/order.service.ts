import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Order, Pagable, Page } from '@app/model';
import { environment } from '@environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private orderUrl = `${environment.apiUrl}/order`;

  constructor(private http: HttpClient) {}

  getOrder(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.orderUrl}/${orderId}`);
  }

  getUserOrder(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.orderUrl}/user/${orderId}`);
  }

  getActiveOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.orderUrl}/active`);
  }

  getHistoryOrders(page: Page): Observable<Pagable<Order>> {
    return this.http.get<Pagable<Order>>(
      `${this.orderUrl}/history?page=${page.number}&size=${page.size}`
    );
  }

  createOrder(order: Order): Observable<Order> {
    return this.http.post<Order>(`${this.orderUrl}`, order);
  }

  deleteOrder(orderId: number): Observable<Order> {
    return this.http.delete<Order>(`${this.orderUrl}/${orderId}`);
  }

  getMatchedSellOrders(order: Order, page: Page): Observable<Pagable<Order>> {
    return this.http.post<Pagable<Order>>(
      `${this.orderUrl}/matched?page=${page.number}&size=${page.size}`,
      order
    );
  }

  trade(order: Order, matchedOrderId: number): Observable<Order> {
    return this.http.post<Order>(
      `${this.orderUrl}/trade/${matchedOrderId}`,
      order
    );
  }
}
