import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { emptyOrder, Order, Page, Paginator } from '@app/model';
import { OrderService } from '@app/service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  order: Order = emptyOrder;
  orderId = 0;
  page: Page = { number: 0, size: 5 };
  matchedOrders: Order[] = [];

  paginator: Paginator = {
    length: 0,
    pageSize: 5,
    pageIndex: 0,
    pageSizeOptions: [5, 10, 25],
  };

  constructor(
    private orderService: OrderService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const orderId = this.route.snapshot.paramMap.get('id');
    if (orderId == null) {
      this.router.navigate(['/orders']);
      return;
    }
    this.orderId = +orderId;
    this.getOrder();
  }

  private getOrder() {
    this.orderService.getUserOrder(this.orderId).subscribe(order => {
      this.order = order;
      if (order.type === 'BUY') {
        this.getMatchedSellOrders();
      }
    });
  }

  getMatchedSellOrders() {
    this.orderService.getMatchedSellOrders(this.order, this.page).subscribe({
      next: ordersPage => {
        this.matchedOrders = ordersPage.content;
        this.paginator.length = ordersPage.totalElements;
        this.paginator.pageSize = ordersPage.size;
        this.paginator.pageIndex = ordersPage.number;
      },
    });
  }

  handlePageEvent(e: PageEvent) {
    this.page.size = e.pageSize;
    this.page.number = e.pageIndex;
    this.getMatchedSellOrders();
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.paginator.pageSizeOptions = setPageSizeOptionsInput
        .split(',')
        .map(str => +str);
    }
  }

  calculateTotalPrice(order: Order) {
    let totalPrice = 0;
    order.trades.forEach(trade => {
      totalPrice += trade.price.amount * trade.quantity;
    });
    return totalPrice;
  }

  deleteOrder() {
    this.orderService.deleteOrder(this.orderId).subscribe({
      complete: () => {
        this.getOrder();
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

  trade(matchedOrder: Order) {
    this.orderService.trade(this.order, matchedOrder.id).subscribe({
      complete: () => {
        this.getOrder();
        this.toastr.success('Order traded');
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
}
