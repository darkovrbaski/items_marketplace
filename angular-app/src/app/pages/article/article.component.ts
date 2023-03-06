import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Article, emptyArticle } from 'src/app/model/article';
import { Order, emptyOrder } from 'src/app/model/order';
import { OrderBook, emptyOrderBook } from 'src/app/model/orderBook';
import { ArticleService } from 'src/app/service/article.service';
import { AuthService } from 'src/app/service/auth.service';
import { OrderBookService } from 'src/app/service/order-book.service';
import { OrderService } from 'src/app/service/order.service';

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
