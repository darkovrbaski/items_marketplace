import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Pagable } from '../model/pagable';
import { Page } from '../model/page';
import { RegistrationRequest } from '../model/registrationRequest';
import { User } from '../model/user';

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
