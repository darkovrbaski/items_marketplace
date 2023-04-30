import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ArticleItem, emptyArticleItem } from 'src/app/model/articleItem';
import { Order, OrderType, emptyOrder } from 'src/app/model/order';
import { Page } from 'src/app/model/page';
import { Paginator } from 'src/app/model/paginator';
import { AuthService } from 'src/app/service/auth.service';
import { InventoryService } from 'src/app/service/inventory.service';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-inventory-card-view',
  templateUrl: './inventory-card-view.component.html',
  styleUrls: ['./inventory-card-view.component.scss'],
})
export class InventoryCardViewComponent implements OnInit {
  @Input() articleItems: ArticleItem[] = [];
  @Input() userId = 1;
  page: Page = { number: 0, size: 5 };

  paginator: Paginator = {
    length: 0,
    pageSize: 5,
    pageIndex: 0,
    pageSizeOptions: [5, 10, 25],
  };

  searchInput = '';
  selectedItem: ArticleItem = emptyArticleItem;

  newOrder: Order = emptyOrder;
  enabledDiscount = false;
  discount = 0;

  constructor(
    private inventoryService: InventoryService,
    private router: Router,
    private authService: AuthService,
    private orderService: OrderService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.getArticleItems();
  }

  getArticleItems() {
    this.inventoryService
      .getArticleItems(this.page)
      .subscribe(articleItemsPage => {
        this.articleItems = articleItemsPage.content;
        this.paginator.length = articleItemsPage.totalElements;
        this.paginator.pageIndex = articleItemsPage.number;
        this.selectedItem = this.articleItems[0];
      });
  }

  handlePageEvent(e: PageEvent) {
    this.page.number = e.pageIndex;
    if (this.searchInput === '') {
      this.getArticleItems();
    } else {
      this.getSearchedArticleItems();
    }
  }

  serachArticleItems() {
    if (this.searchInput === '') {
      this.getArticleItems();
      return;
    }
    this.page.number = 0;
    this.getSearchedArticleItems();
  }

  getSearchedArticleItems() {
    this.inventoryService
      .getSearchArticleItems(this.searchInput, this.page)
      .subscribe(articleItemsPage => {
        this.articleItems = articleItemsPage.content;
        this.paginator.length = articleItemsPage.totalElements;
        this.paginator.pageIndex = articleItemsPage.number;
      });
  }

  selectItem(articleItem: ArticleItem) {
    this.selectedItem = articleItem;
  }

  routeToArticlePage() {
    this.router.navigate(['article', this.selectedItem.article.name]);
  }

  placeOrder() {
    this.newOrder.article = this.selectedItem.article;
    this.newOrder.user = this.authService.userValue;
    this.newOrder.type = OrderType.SELL;
    this.newOrder.enabledAutoTrade = true;
    if (!this.enabledDiscount) {
      this.newOrder.lowerSellPrice.amount = this.newOrder.price.amount;
      this.discount = 0;
    }
    this.orderService.createOrder(this.newOrder).subscribe({
      complete: () => {
        this.newOrder = emptyOrder;
        this.toastr.success('Order created');
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

  calculateMinimumReceive() {
    if (this.newOrder.lowerSellPrice.amount === 0) {
      return this.newOrder.quantity * this.newOrder.price.amount;
    }
    return this.newOrder.quantity * this.newOrder.lowerSellPrice.amount;
  }

  calculateLowerSellPrice() {
    this.newOrder.lowerSellPrice.amount =
      this.newOrder.price.amount -
      this.newOrder.price.amount * (this.discount / 100);
    if (!this.enabledDiscount) {
      this.newOrder.lowerSellPrice.amount = this.newOrder.price.amount;
    }
  }

  calculateProcentalDiscount() {
    this.discount =
      ((this.newOrder.price.amount - this.newOrder.lowerSellPrice.amount) /
        this.newOrder.price.amount) *
      100;
    if (!this.enabledDiscount) {
      this.discount = 0;
    }
  }

  changeEnabledDiscount() {
    this.calculateLowerSellPrice();
    this.calculateProcentalDiscount();
  }
}
