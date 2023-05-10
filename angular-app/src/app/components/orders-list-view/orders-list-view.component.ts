import { Component, Input } from '@angular/core';
import { Order } from '@app/model';
import { OrderService } from '@app/service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-orders-list-view',
  templateUrl: './orders-list-view.component.html',
  styleUrls: ['./orders-list-view.component.scss'],
})
export class OrdersListViewComponent {
  @Input() orders: Order[] = [];

  constructor(
    private orderService: OrderService,
    private toastr: ToastrService
  ) {}

  deleteOrder(orderId: number) {
    this.orderService.deleteOrder(orderId).subscribe({
      complete: () => {
        this.orders = this.orders.filter(order => order.id !== orderId);
        this.toastr.success('Order canceled');
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

  calculateTotalPrice(order: Order) {
    let totalPrice = 0;
    order.trades.forEach(trade => {
      totalPrice += trade.price.amount * trade.quantity;
    });
    return totalPrice;
  }
}
