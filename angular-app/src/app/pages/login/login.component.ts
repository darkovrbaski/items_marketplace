import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthRequest } from '@app/model';
import { AuthService } from '@app/service';
import { ToastrService } from 'ngx-toastr';
import { first } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  loading = false;
  submitted = false;
  authRequest = {} as AuthRequest;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private toastr: ToastrService
  ) {
    if (this.authService.userValue) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    this.authRequest.username = this.f['username'].value;
    this.authRequest.password = this.f['password'].value;

    this.loading = true;
    this.authService
      .login(this.authRequest)
      .pipe(first())
      .subscribe({
        complete: () => {
          let returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
          if (Array.isArray(returnUrl)) {
            returnUrl = returnUrl[0];
          }
          this.router.navigate([returnUrl]);
        },
        error: error => {
          let errors = '';
          error.error.errors.forEach((message: string) => {
            errors += `${message}</br>`;
          });
          this.toastr.error(errors, error.error.message, {
            enableHtml: true,
          });
          this.loading = false;
        },
      });
  }
}
