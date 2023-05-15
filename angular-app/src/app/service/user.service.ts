import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Pagable, Page, RegistrationRequest, User } from '@app/model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private userUrl = `${environment.apiUrl}/user`;

  constructor(private http: HttpClient) {}

  getUser(username: string): Observable<User> {
    return this.http.get<User>(`${this.userUrl}/${username}`);
  }

  getUsers(page: Page): Observable<Pagable<User>> {
    return this.http.get<Pagable<User>>(
      `${this.userUrl}/all?page=${page.number}&size=${page.size}`
    );
  }

  searchUsers(username: string, page: Page): Observable<Pagable<User>> {
    return this.http.get<Pagable<User>>(
      `${this.userUrl}/search?username=${username}&page=${page.number}&size=${page.size}`
    );
  }

  deleteUser(userId: number): Observable<User> {
    return this.http.delete<User>(`${this.userUrl}/${userId}`);
  }

  updateUser(user: RegistrationRequest): Observable<User> {
    return this.http.put<User>(`${this.userUrl}`, user);
  }
}
