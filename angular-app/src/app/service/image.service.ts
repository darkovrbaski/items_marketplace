import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from 'src/environments/environment.development';
import { Article } from '../model/article';

@Injectable({
  providedIn: 'root',
})
export class ImageService {
  private userImageUrl = `${environment.apiUrl}/user/image`;
  private articleImageUrl = `${environment.apiUrl}/article/image`;

  constructor(private http: HttpClient) {}

  uploadUserImage(imageFormData: FormData): Observable<unknown> {
    return this.http.post(this.userImageUrl, imageFormData);
  }

  uploadArticleImage(
    imageFormData: FormData,
    article: Article
  ): Observable<unknown> {
    return this.http.post(
      `${this.articleImageUrl}/${article.name}`,
      imageFormData
    );
  }
}
