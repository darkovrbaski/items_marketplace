import { NgModule } from '@angular/core';
import { MatPaginatorModule } from '@angular/material/paginator';

export const materialComponents = [MatPaginatorModule];

@NgModule({
  imports: [materialComponents],
  exports: [materialComponents],
})
export class MaterialModule {}
