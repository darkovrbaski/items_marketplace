import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from 'src/app/app-routing.module';
import { HasRoleDirective } from 'src/app/directive/has-role.directive';
import { MaterialModule } from 'src/app/material/material.module';

import { ArticlesListViewComponent } from './articles-list-view.component';

describe('ArticlesListViewComponent', () => {
  let component: ArticlesListViewComponent;
  let fixture: ComponentFixture<ArticlesListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArticlesListViewComponent, HasRoleDirective],
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
    }).compileComponents();

    fixture = TestBed.createComponent(ArticlesListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
