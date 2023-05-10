import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Article } from '@app/model';
import { environment } from '@environments/environment.development';
import { Observable } from 'rxjs';

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
