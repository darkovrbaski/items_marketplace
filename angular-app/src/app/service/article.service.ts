import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Article, Pagable, Page } from '@app/model';
import { environment } from '@environments/environment.development';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private articleUrl = `${environment.apiUrl}/article`;

  constructor(private http: HttpClient) {}

  getArticles(page: Page): Observable<Pagable<Article>> {
    return this.http.get<Pagable<Article>>(
      `${this.articleUrl}?page=${page.number}&size=${page.size}`
    );
  }

  getArticle(articleId: number): Observable<Article> {
    return this.http.get<Article>(`${this.articleUrl}/${articleId}`);
  }

  getArticleByName(name: string): Observable<Article> {
    return this.http.get<Article>(`${this.articleUrl}/find/${name}`);
  }

  getSearchArticles(name: string, page: Page): Observable<Pagable<Article>> {
    return this.http.get<Pagable<Article>>(
      `${this.articleUrl}/search?name=${name}&page=${page.number}&size=${page.size}`
    );
  }

  createArticle(article: Article): Observable<Article> {
    return this.http.post<Article>(`${this.articleUrl}`, article);
  }

  updateArticle(article: Article): Observable<Article> {
    return this.http.put<Article>(`${this.articleUrl}`, article);
  }

  deleteArticle(articleId: number): Observable<Article> {
    return this.http.delete<Article>(`${this.articleUrl}/${articleId}`);
  }
}
