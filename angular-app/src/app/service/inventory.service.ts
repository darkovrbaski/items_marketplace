import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ArticleItem, Pagable, Page } from '@app/model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class InventoryService {
  private inventoryUrl = `${environment.apiUrl}/inventory`;

  constructor(private http: HttpClient) {}

  getArticleItems(page: Page): Observable<Pagable<ArticleItem>> {
    return this.http.get<Pagable<ArticleItem>>(
      `${this.inventoryUrl}?page=${page.number}&size=${page.size}`
    );
  }

  getSearchArticleItems(
    name: string,
    page: Page
  ): Observable<Pagable<ArticleItem>> {
    return this.http.get<Pagable<ArticleItem>>(
      `${this.inventoryUrl}/search?name=${name}&page=${page.number}&size=${page.size}`
    );
  }
}
