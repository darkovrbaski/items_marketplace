import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ArticlesListViewComponent } from './components/articles-list-view/articles-list-view.component';
import { InventoryCardViewComponent } from './components/inventory-card-view/inventory-card-view.component';
import { HomepageComponent } from './pages/homepage/homepage.component';

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
    component: InventoryCardViewComponent,
  },
  { path: '**', redirectTo: '' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
