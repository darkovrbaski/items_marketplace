import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { emptyUser, User } from '@app/model';
import { UserService } from '@app/service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  user: User = emptyUser;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.getUser();
  }

  getUser() {
    const username = this.route.snapshot.paramMap.get('username');
    if (username == null) {
      this.router.navigate(['/users']);
      return;
    }
    this.userService.getUser(username).subscribe({
      next: user => {
        this.user = user;
      },
      error: () => {
        this.router.navigate(['/users']);
      },
    });
  }
}
