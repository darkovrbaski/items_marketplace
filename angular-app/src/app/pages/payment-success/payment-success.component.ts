import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.scss'],
})
export class PaymentSuccessComponent {
  constructor(private router: Router, private toastr: ToastrService) {
    this.toastr.success('Payment successful');
    router.navigate(['/wallet']);
  }
}
