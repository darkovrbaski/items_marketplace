import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { MaterialModule } from './material/material.module';

import { AppComponent } from './app.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { WalletComponent } from './pages/wallet/wallet.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ArticlesListViewComponent } from './components/articles-list-view/articles-list-view.component';
import { UserWalletInfoComponent } from './components/user-wallet-info/user-wallet-info.component';
import { InventoryCardViewComponent } from './components/inventory-card-view/inventory-card-view.component';
import { OrdersListViewComponent } from './components/orders-list-view/orders-list-view.component';
import { OrdersComponent } from './pages/orders/orders.component';
import { ArticleComponent } from './pages/article/article.component';
import { ArticlesComponent } from './pages/articles/articles.component';
import { ErrorInterceptor } from './interceptor/error.interceptor';
import { JwtInterceptor } from './interceptor/jwt.interceptor';
import { LoginComponent } from './pages/login/login.component';
import { HasRoleDirective } from './directive/has-role.directive';

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
