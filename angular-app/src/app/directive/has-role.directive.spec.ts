import { TemplateRef, ViewContainerRef } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { HasRoleDirective } from './has-role.directive';

describe('HasRoleDirective', () => {
  let templateRef: TemplateRef<unknown>;
  let viewContainer: ViewContainerRef;
  let authService: AuthService;

  it('should create an instance', () => {
    const directive = new HasRoleDirective(
      templateRef,
      viewContainer,
      authService
    );
    expect(directive).toBeTruthy();
  });
});
