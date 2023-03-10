import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { ArticleItem, emptyArticleItem } from 'src/app/model/articleItem';
import { Page } from 'src/app/model/page';
import { Paginator } from 'src/app/model/paginator';
import { InventoryService } from 'src/app/service/inventory.service';

@Component({
  selector: 'app-inventory-card-view',
  templateUrl: './inventory-card-view.component.html',
  styleUrls: ['./inventory-card-view.component.scss'],
})
export class InventoryCardViewComponent implements OnInit {
  @Input() articleItems: ArticleItem[] = [];
  @Input() userId = 1;
  page: Page = { number: 0, size: 12 };

  paginator: Paginator = {
    length: 0,
    pageSize: 12,
    pageIndex: 0,
    pageSizeOptions: [],
  };

  searchInput = '';
  selectedItem: ArticleItem = emptyArticleItem;

  constructor(
    private inventoryService: InventoryService,
    private router: Router
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
}
