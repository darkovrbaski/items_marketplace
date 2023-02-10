import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { User, emptyUser } from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private userUrl = `${environment.apiUrl}/user`;
  user: BehaviorSubject<User> = new BehaviorSubject<User>(emptyUser);

  constructor(private http: HttpClient) {
    this.getUser(1).subscribe(user => {
      this.user.next(user);
    });
  }

  getUser(userId: number): Observable<User> {
    return this.http.get<User>(`${this.userUrl}/${userId}`);
  }
}
