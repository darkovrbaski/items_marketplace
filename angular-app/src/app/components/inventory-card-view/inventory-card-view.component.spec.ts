import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from '@app/app-routing.module';
import { MaterialModule } from '@app/material';
import { ToastrModule } from 'ngx-toastr';

import { InventoryCardViewComponent } from './inventory-card-view.component';

describe('InventoryCardViewComponent', () => {
  let component: InventoryCardViewComponent;
  let fixture: ComponentFixture<InventoryCardViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InventoryCardViewComponent],
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

    fixture = TestBed.createComponent(InventoryCardViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
