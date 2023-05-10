import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  Article,
  emptyArticle,
  emptyOrder,
  emptyOrderBook,
  Order,
  OrderBook,
  OrderType,
} from '@app/model';
import {
  ArticleService,
  AuthService,
  OrderBookService,
  OrderService,
} from '@app/service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-article',
  templateUrl: './article.component.html',
  styleUrls: ['./article.component.scss'],
})
export class ArticleComponent implements OnInit {
  article: Article = emptyArticle;
  orderBook: OrderBook = emptyOrderBook;
  newOrder: Order = emptyOrder;

  constructor(
    private articleService: ArticleService,
    private orderBookService: OrderBookService,
    private orderService: OrderService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    const articleName = this.route.snapshot.paramMap.get('name');
    if (articleName == null) {
      this.router.navigate(['/articles']);
      return;
    }
    this.articleService.getArticleByName(articleName).subscribe(article => {
      this.article = article;
      this.getOrderBook();
    });
  }

  getOrderBook() {
    this.orderBookService.getOrderBook(this.article.id).subscribe(orderBook => {
      this.orderBook = orderBook;
    });
  }

  placeOrder() {
    this.newOrder.article = this.article;
    this.newOrder.user = this.authService.userValue;
    this.newOrder.type = OrderType.BUY;
    this.orderService.createOrder(this.newOrder).subscribe({
      complete: () => {
        this.getOrderBook();
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

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }
}
