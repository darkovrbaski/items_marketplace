import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-payment-failed',
  templateUrl: './payment-failed.component.html',
  styleUrls: ['./payment-failed.component.scss'],
})
export class PaymentFailedComponent {
  constructor(private router: Router, private toastr: ToastrService) {
    this.toastr.error('Payment failed');
    router.navigate(['/wallet']);
  }
}
