import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Order } from 'src/app/model/order';
import { Page } from 'src/app/model/page';
import { Paginator } from 'src/app/model/paginator';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
})
export class OrdersComponent implements OnInit {
  activeOrders: Order[] = [];
  historyOrders: Order[] = [];

  page: Page = { number: 0, size: 5 };

  paginator: Paginator = {
    length: 0,
    pageSize: 5,
    pageIndex: 0,
    pageSizeOptions: [5, 10, 25],
  };

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderService.getActiveOrders().subscribe(orders => {
      this.activeOrders = orders;
    });
  }

  getHistoryOrders() {
    this.orderService.getHistoryOrders(this.page).subscribe(ordersPage => {
      this.historyOrders = ordersPage.content;
      this.paginator.length = ordersPage.totalElements;
      this.paginator.pageSize = ordersPage.size;
      this.paginator.pageIndex = ordersPage.number;
    });
  }

  handlePageEvent(e: PageEvent) {
    this.page.size = e.pageSize;
    this.page.number = e.pageIndex;
    this.getHistoryOrders();
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.paginator.pageSizeOptions = setPageSizeOptionsInput
        .split(',')
        .map(str => +str);
    }
  }

  handleHistoryTabEvent() {
    if (this.historyOrders.length === 0) {
      this.getHistoryOrders();
    }
  }
}
