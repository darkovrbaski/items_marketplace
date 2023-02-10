import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ArticlesListViewComponent } from './components/articles-list-view/articles-list-view.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { InventoryCardViewComponent } from './components/inventory-card-view/inventory-card-view.component';
import { UserWalletInfoComponent } from './components/user-wallet-info/user-wallet-info.component';
import { MaterialModule } from './material/material.module';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { WalletComponent } from './pages/wallet/wallet.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        MaterialModule,
      ],
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
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'angular-app'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('angular-app');
  });
});
