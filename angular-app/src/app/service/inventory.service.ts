import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { ArticleItem } from '../model/articleItem';
import { Pagable } from '../model/pagable';
import { Page } from '../model/page';

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
