import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ArticlesListViewComponent } from './components/articles-list-view';
import { FooterComponent } from './components/footer';
import { HeaderComponent } from './components/header';
import { InventoryCardViewComponent } from './components/inventory-card-view';
import { OrdersListViewComponent } from './components/orders-list-view';
import { UserListViewComponent } from './components/user-list-view';
import { UserWalletInfoComponent } from './components/user-wallet-info';
import { HasRoleDirective } from './helper/directive';
import { ErrorInterceptor, JwtInterceptor } from './helper/interceptor';
import { MaterialModule } from './material';
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

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    HeaderComponent,
    FooterComponent,
    InventoryComponent,
    ProfileComponent,
    WalletComponent,
    UserWalletInfoComponent,
    InventoryCardViewComponent,
    ArticlesListViewComponent,
    OrdersListViewComponent,
    OrdersComponent,
    ArticleComponent,
    ArticlesComponent,
    LoginComponent,
    HasRoleDirective,
    UserListViewComponent,
    UsersComponent,
    RegisterComponent,
    UserComponent,
    PaymentSuccessComponent,
    PaymentFailedComponent,
    OrderComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    MaterialModule,
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
