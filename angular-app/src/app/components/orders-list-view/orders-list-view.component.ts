import { Component, Input } from '@angular/core';
import { Order } from 'src/app/model/order';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-orders-list-view',
  templateUrl: './orders-list-view.component.html',
  styleUrls: ['./orders-list-view.component.scss'],
})
export class OrdersListViewComponent {
  @Input() orders: Order[] = [];

  constructor(private orderService: OrderService) {}

  deleteOrder(orderId: number) {
    this.orderService.deleteOrder(orderId).subscribe(() => {
      this.orders = this.orders.filter(order => order.id !== orderId);
    });
  }
}
