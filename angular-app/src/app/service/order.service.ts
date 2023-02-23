import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Order } from '../model/order';
import { Pagable } from '../model/pagable';
import { Page } from '../model/page';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private orderUrl = `${environment.apiUrl}/order`;

  constructor(private http: HttpClient) {}

  getOrder(orderId: number): Observable<Order> {
    return this.http.get<Order>(`${this.orderUrl}/${orderId}`);
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
}
