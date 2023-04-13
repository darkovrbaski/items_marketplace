import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, AbstractControlOptions } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { first } from 'rxjs';
import { RegistrationRequest } from 'src/app/model/registrationRequest';
import { AuthService } from 'src/app/service/auth.service';
import {
  EmailValidation,
  PasswordValidation,
  RepeatPasswordValidator,
  UsernameValidation,
} from 'src/app/validator/validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  loading = false;
  submitted = false;
  registrationRequest = {} as RegistrationRequest;

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
    this.registerForm = this.formBuilder.group(
      {
        username: ['', UsernameValidation],
        password: ['', PasswordValidation],
        email: ['', EmailValidation],
        firstName: [''],
        lastName: [''],
        repeatPassword: [''],
      },
      { validator: RepeatPasswordValidator } as AbstractControlOptions
    );
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    this.registrationRequest.username = this.f['username'].value;
    this.registrationRequest.password = this.f['password'].value;
    this.registrationRequest.email = this.f['email'].value;
    this.registrationRequest.firstName = this.f['firstName'].value;
    this.registrationRequest.lastName = this.f['lastName'].value;

    this.loading = true;
    this.authService
      .register(this.registrationRequest)
      .pipe(first())
      .subscribe({
        complete: () => {
          const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
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
