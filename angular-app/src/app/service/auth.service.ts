import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {
  AuthRequest,
  AuthResponse,
  RegistrationRequest,
  User,
} from '@app/model';
import { environment } from '@environments/environment';
import { BehaviorSubject, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private authUrl = `${environment.apiUrl}/auth`;
  private userUrl = `${environment.apiUrl}/user`;

  user: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  token: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(
    localStorage.getItem('token')
  );

  constructor(private http: HttpClient, private router: Router) {}

  get tokenValue() {
    return this.token.value;
  }

  get userValue() {
    return this.user.value;
  }

  login(authRequest: AuthRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authUrl}/login`, authRequest)
      .pipe(
        map(authResponse => {
          localStorage.setItem('token', authResponse.token);
          this.token.next(authResponse.token);
          this.getCurrentUser().subscribe(user => {
            this.user.next(user);
          });
          return authResponse;
        })
      );
  }

  register(registrationRequest: RegistrationRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authUrl}/register`, registrationRequest)
      .pipe(
        map(authResponse => {
          localStorage.setItem('token', authResponse.token);
          this.token.next(authResponse.token);
          this.getCurrentUser().subscribe(user => {
            this.user.next(user);
          });
          return authResponse;
        })
      );
  }

  registerAdmin(
    registrationRequest: RegistrationRequest
  ): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.authUrl}/register/admin`,
      registrationRequest
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.token.next(null);
    this.user.next(null);
    this.router.navigate(['/login']);
  }

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.userUrl}`).pipe(
      map(user => {
        this.user.next(user);
        return user;
      })
    );
  }

  isLoggedIn(): boolean {
    return this.tokenValue != null;
  }
}
