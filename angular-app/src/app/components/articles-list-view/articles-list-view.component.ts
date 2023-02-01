import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { Article, emptyArticle } from 'src/app/model/article';
import { Page } from 'src/app/model/page';
import { Paginator } from 'src/app/model/paginator';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-articles-list-view',
  templateUrl: './articles-list-view.component.html',
  styleUrls: ['./articles-list-view.component.scss'],
})
export class ArticlesListViewComponent implements OnInit {
  @Input() articles: Article[] = [];
  page: Page = { number: 0, size: 5 };

  paginator: Paginator = {
    length: 0,
    pageSize: 5,
    pageIndex: 0,
    pageSizeOptions: [5, 10, 25],
  };

  searchInput = '';

  newArticle: Article = emptyArticle;
  showAdd = false;

  editArticle: Article = emptyArticle;
  showEdit = false;

  constructor(
    private articleService: ArticleService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.getArticles();
  }

  getArticles() {
    this.articleService.getArticles(this.page).subscribe(articlesPage => {
      this.articles = articlesPage.content;
      this.paginator.length = articlesPage.totalElements;
      this.paginator.pageSize = articlesPage.size;
      this.paginator.pageIndex = articlesPage.number;
    });
  }

  handlePageEvent(e: PageEvent) {
    this.page.size = e.pageSize;
    this.page.number = e.pageIndex;
    if (this.searchInput === '') {
      this.getArticles();
    } else {
      this.getSearchedArticles();
    }
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.paginator.pageSizeOptions = setPageSizeOptionsInput
        .split(',')
        .map(str => +str);
    }
  }

  serachArticles() {
    if (this.searchInput === '') {
      this.getArticles();
      return;
    }
    this.page.number = 0;
    this.getSearchedArticles();
  }

  getSearchedArticles() {
    this.articleService
      .getSearchArticles(this.searchInput, this.page)
      .subscribe(articlesPage => {
        this.articles = articlesPage.content;
        this.paginator.length = articlesPage.totalElements;
        this.paginator.pageSize = articlesPage.size;
        this.paginator.pageIndex = articlesPage.number;
      });
  }

  addToogle() {
    this.showAdd = !this.showAdd;
  }

  createArticle() {
    this.articleService.createArticle(this.newArticle).subscribe({
      complete: () => {
        this.getArticles();
        this.newArticle = emptyArticle;
        this.toastr.success('Article created');
        this.showAdd = false;
      },
      error: error => {
        this.toastr.error(error.error.message);
      },
    });
  }

  deleteArticle(articleId: number) {
    this.articleService.deleteArticle(articleId).subscribe({
      complete: () => {
        this.getArticles();
        this.toastr.success('Article deleted');
      },
      error: error => {
        this.toastr.error(error.error.message);
      },
    });
  }

  editToogle(article: Article) {
    this.editArticle = article;
    this.showEdit = !this.showEdit;
  }

  updateArticle(article: Article) {
    this.articleService.updateArticle(article).subscribe({
      complete: () => {
        this.getArticles();
        this.editArticle = emptyArticle;
        this.toastr.success('Article updated');
        this.showEdit = false;
      },
      error: error => {
        this.toastr.error(error.error.message);
      },
    });
  }
}
