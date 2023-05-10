import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '@app/model';
import { AuthService } from '@app/service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  user!: User | null;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (this.isLoggedIn()) {
      this.authService.getCurrentUser().subscribe(user => {
        this.user = user;
      });
    }
    this.authService.user.subscribe(user => (this.user = user));
  }

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
  }

  login() {
    this.router.navigate(['/login']);
  }

  register() {
    this.router.navigate(['/register']);
  }

  showLogin() {
    return !this.isLoggedIn() && !this.router.url.startsWith('/login');
  }

  showRegister() {
    return !this.isLoggedIn() && this.router.url !== '/register';
  }
}
