import { inject } from '@angular/core';
import { RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '@app/service';

export const authGuard = (state: RouterStateSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    return true;
  }

  return router.navigate(['/login'], {
    queryParams: { returnUrl: state.url },
  });
};
