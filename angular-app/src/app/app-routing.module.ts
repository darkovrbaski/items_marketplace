import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { authGuard } from './helper/guard';
import { ArticleComponent } from './pages/article';
import { ArticlesComponent } from './pages/articles';
import { HomepageComponent } from './pages/homepage';
import { InventoryComponent } from './pages/inventory';
import { LoginComponent } from './pages/login';
import { OrderComponent } from './pages/order';
import { OrdersComponent } from './pages/orders';
import { PaymentFailedComponent } from './pages/payment-failed';
import { PaymentSuccessComponent } from './pages/payment-success';
import { ProfileComponent } from './pages/profile';
import { RegisterComponent } from './pages/register';
import { UserComponent } from './pages/user';
import { UsersComponent } from './pages/users';
import { WalletComponent } from './pages/wallet';

const routes: Routes = [
  {
    path: '',
    component: HomepageComponent,
  },
  {
    path: 'articles',
    component: ArticlesComponent,
  },
  {
    path: 'inventory',
    component: InventoryComponent,
    canActivate: [authGuard],
  },
  {
    path: 'wallet',
    component: WalletComponent,
    canActivate: [authGuard],
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [authGuard],
  },
  {
    path: 'orders',
    component: OrdersComponent,
    canActivate: [authGuard],
  },
  {
    path: 'article/:name',
    component: ArticleComponent,
    canActivate: [authGuard],
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'users',
    canActivate: [authGuard],
    component: UsersComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'user/:username',
    canActivate: [authGuard],
    component: UserComponent,
  },
  {
    path: 'payment/success',
    canActivate: [authGuard],
    component: PaymentSuccessComponent,
  },
  {
    path: 'payment/failed',
    canActivate: [authGuard],
    component: PaymentFailedComponent,
  },
  {
    path: 'order/:id',
    canActivate: [authGuard],
    component: OrderComponent,
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
