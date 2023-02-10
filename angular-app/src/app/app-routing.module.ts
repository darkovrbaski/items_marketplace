import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticlesListViewComponent } from './components/articles-list-view/articles-list-view.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { WalletComponent } from './pages/wallet/wallet.component';

const routes: Routes = [
  {
    path: '',
    component: HomepageComponent,
  },
  {
    path: 'articles',
    component: ArticlesListViewComponent,
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
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
