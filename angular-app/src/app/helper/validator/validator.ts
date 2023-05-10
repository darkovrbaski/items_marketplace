import { AbstractControl, FormGroup, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';

export const EmailValidation = [Validators.required, Validators.email];

export const PasswordValidation = [
  Validators.required,
  Validators.minLength(3),
];

export const UsernameValidation = [
  Validators.required,
  Validators.minLength(3),
];

export class RepeatPasswordEStateMatcher implements ErrorStateMatcher {
  isErrorState(control: AbstractControl<string, string>): boolean {
    return (
      control &&
      control.parent?.get('password')?.value !==
        control.parent?.get('passwordAgain')?.value &&
      control.dirty
    );
  }
}

export function RepeatPasswordValidator(group: FormGroup) {
  const password = group.controls['password'].value;
  const passwordConfirmation = group.controls['repeatPassword'].value;

  return password === passwordConfirmation ? null : { passwordsNotEqual: true };
}
