import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticleComponent } from './pages/article/article.component';
import { ArticlesComponent } from './pages/articles/articles.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { LoginComponent } from './pages/login/login.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { PaymentFailedComponent } from './pages/payment-failed/payment-failed.component';
import { PaymentSuccessComponent } from './pages/payment-success/payment-success.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';
import { UserComponent } from './pages/user/user.component';
import { UsersComponent } from './pages/users/users.component';
import { WalletComponent } from './pages/wallet/wallet.component';

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
  },
  {
    path: 'wallet',
    component: WalletComponent,
  },
  {
    path: 'profile',
    component: ProfileComponent,
  },
  {
    path: 'orders',
    component: OrdersComponent,
  },
  {
    path: 'article/:name',
    component: ArticleComponent,
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  {
    path: 'users',
    component: UsersComponent,
  },
  {
    path: 'register',
    component: RegisterComponent,
  },
  {
    path: 'user/:username',
    component: UserComponent,
  },
  {
    path: 'payment/success',
    component: PaymentSuccessComponent,
  },
  {
    path: 'payment/failed',
    component: PaymentFailedComponent,
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
