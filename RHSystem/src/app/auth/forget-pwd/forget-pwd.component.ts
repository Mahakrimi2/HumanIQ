import { Component } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forget-pwd',
  templateUrl: './forget-pwd.component.html',
  styleUrls: ['./forget-pwd.component.css']
})
export class ForgetPwdComponent {
successMessage: any;
errorMessage: any;
onSubmit() {
throw new Error('Method not implemented.');
}
forgotPasswordForm: FormGroup<any> | undefined;

  constructor(private router: Router) {
    this.router=router
    
  }

goToLogin() {
this.router.navigate(['/auth/Login']);
}

}
