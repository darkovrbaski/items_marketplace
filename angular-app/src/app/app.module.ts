import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
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
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
