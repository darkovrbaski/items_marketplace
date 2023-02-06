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

  getArticleItems(
    userId: number,
    page: Page
  ): Observable<Pagable<ArticleItem>> {
    return this.http.get<Pagable<ArticleItem>>(
      `${this.inventoryUrl}/${userId}?page=${page.number}&size=${page.size}`
    );
  }

  getSearchArticleItems(
    userId: number,
    name: string,
    page: Page
  ): Observable<Pagable<ArticleItem>> {
    return this.http.get<Pagable<ArticleItem>>(
      `${this.inventoryUrl}/search/${userId}?name=${name}&page=${page.number}&size=${page.size}`
    );
  }
}
