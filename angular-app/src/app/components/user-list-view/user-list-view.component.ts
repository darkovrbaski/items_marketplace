import { Component, Input, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router } from '@angular/router';
import { emptyUser, Page, Paginator, User } from '@app/model';
import { AuthService, UserService } from '@app/service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-list-view',
  templateUrl: './user-list-view.component.html',
  styleUrls: ['./user-list-view.component.scss'],
})
export class UserListViewComponent implements OnInit {
  @Input() users: User[] = [];
  page: Page = { number: 0, size: 5 };

  paginator: Paginator = {
    length: 0,
    pageSize: 5,
    pageIndex: 0,
    pageSizeOptions: [5, 10, 25],
  };

  searchInput = '';

  newUser: User = emptyUser;
  showAdd = false;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toastr: ToastrService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers(this.page).subscribe(usersPage => {
      this.users = usersPage.content;
      this.paginator.length = usersPage.totalElements;
      this.paginator.pageSize = usersPage.size;
      this.paginator.pageIndex = usersPage.number;
    });
  }

  handlePageEvent(e: PageEvent) {
    this.page.size = e.pageSize;
    this.page.number = e.pageIndex;
    if (this.searchInput === '') {
      this.getUsers();
    } else {
      this.getSearchedUsers();
    }
  }

  setPageSizeOptions(setPageSizeOptionsInput: string) {
    if (setPageSizeOptionsInput) {
      this.paginator.pageSizeOptions = setPageSizeOptionsInput
        .split(',')
        .map(str => +str);
    }
  }

  serachUsers() {
    if (this.searchInput === '') {
      this.getUsers();
      return;
    }
    this.page.number = 0;
    this.getSearchedUsers();
  }

  getSearchedUsers() {
    this.userService
      .searchUsers(this.searchInput, this.page)
      .subscribe(usersPage => {
        this.users = usersPage.content;
        this.paginator.length = usersPage.totalElements;
        this.paginator.pageSize = usersPage.size;
        this.paginator.pageIndex = usersPage.number;
      });
  }

  addToogle() {
    this.showAdd = !this.showAdd;
  }

  createAdmin() {
    this.authService.registerAdmin(this.newUser).subscribe({
      complete: () => {
        this.getUsers();
        this.newUser = emptyUser;
        this.toastr.success('User created');
        this.showAdd = false;
      },
      error: error => {
        let errors = '';
        error.error.errors.forEach((message: string) => {
          errors += `${message}</br>`;
        });
        this.toastr.error(errors, error.error.message, {
          enableHtml: true,
        });
      },
    });
  }

  routeToUserPage(user: User) {
    this.router.navigate(['user', user.username]);
  }
}
