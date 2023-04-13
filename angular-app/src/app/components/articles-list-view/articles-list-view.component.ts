import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { Article, emptyArticle } from 'src/app/model/article';
import { Page } from 'src/app/model/page';
import { Paginator } from 'src/app/model/paginator';
import { ArticleService } from 'src/app/service/article.service';
import { ImageService } from 'src/app/service/image.service';

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

  articleData: Article = emptyArticle;
  showAdd = false;
  showEdit = false;

  uploadedImage: File | null = null;
  loading = false;

  constructor(
    private articleService: ArticleService,
    private toastr: ToastrService,
    private imageService: ImageService
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
    this.articleData.image = '';
    this.articleService.createArticle(this.articleData).subscribe({
      next: newArticle => {
        this.getArticles();
        this.articleData = newArticle;
        this.toastr.success('Article created');
        this.showAdd = false;
        this.imageUploadAction();
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

  deleteArticle(articleId: number) {
    this.articleService.deleteArticle(articleId).subscribe({
      complete: () => {
        this.getArticles();
        this.toastr.success('Article deleted');
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

  editToogle(article: Article) {
    this.articleData = article;
    this.showEdit = !this.showEdit;
  }

  updateArticle(article: Article) {
    this.articleService.updateArticle(article).subscribe({
      complete: () => {
        this.getArticles();
        this.articleData = emptyArticle;
        this.toastr.success('Article updated');
        this.showEdit = false;
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

  onImageUpload(event: any) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {
      this.articleData.image = event.target.result;
      this.uploadedImage = file;
    });

    reader.readAsDataURL(file);
  }

  imageUploadAction() {
    if (this.uploadedImage === null) {
      return;
    }
    const imageFormData = new FormData();
    imageFormData.append('image', this.uploadedImage, this.uploadedImage.name);
    this.loading = true;

    this.imageService
      .uploadArticleImage(imageFormData, this.articleData)
      .subscribe({
        complete: () => {
          this.getArticles();
          this.toastr.success('Image updated');
          this.uploadedImage = null;
          this.loading = false;
        },
        error: error => {
          let errors = '';
          error.error.errors.forEach((message: string) => {
            errors += `${message}</br>`;
          });
          this.toastr.error(errors, error.error.message, {
            enableHtml: true,
          });
          this.uploadedImage = null;
          this.loading = false;
        },
      });
  }
}
