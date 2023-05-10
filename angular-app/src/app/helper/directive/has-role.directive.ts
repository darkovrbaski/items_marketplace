import {
  Directive,
  Input,
  OnInit,
  TemplateRef,
  ViewContainerRef,
} from '@angular/core';
import { User } from '@app/model';
import { AuthService } from '@app/service';

@Directive({
  selector: '[appHasRole]',
})
export class HasRoleDirective implements OnInit {
  currentUser!: User | null;
  requiredRoles!: string[];
  isVisible = false;

  constructor(
    private templateRef: TemplateRef<unknown>,
    private viewContainer: ViewContainerRef,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.authService.user.subscribe(user => {
      this.currentUser = user;
      this.updateView();
    });
  }

  @Input()
  set appHasRole(roles: string[]) {
    this.requiredRoles = roles;
    this.updateView();
  }

  private updateView() {
    if (this.hasRole()) {
      if (!this.isVisible) {
        this.isVisible = true;
        this.viewContainer.createEmbeddedView(this.templateRef);
      }
    } else {
      this.isVisible = false;
      this.viewContainer.clear();
    }
  }

  private hasRole(): boolean {
    if (this.currentUser?.role && this.requiredRoles) {
      return this.requiredRoles.includes(this.currentUser.role);
    }
    return false;
  }
}
