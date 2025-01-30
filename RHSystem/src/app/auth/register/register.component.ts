import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  errorMessage: string = '';
  loading: boolean = false;

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {
    this.registerForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit() {
    console.log('Form Value:', this.registerForm.value);
    console.log('Form Valid:', this.registerForm.valid);
    if (this.registerForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      const formValue = this.registerForm.value;
      const registerData = {
        email: formValue.email,
        password: formValue.password
      };
      
      console.log('Tentative d\'inscription:', registerData.email);
      
      this.authService.register(registerData).subscribe({
        next: (response) => {
          console.log('Inscription réussie');
          this.loading = false;
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error('Erreur d\'inscription:', error);
          this.loading = false;
          if (error.status === 400) {
            this.errorMessage = error.error || 'Erreur lors de l\'inscription';
          } else {
            this.errorMessage = 'Une erreur est survenue lors de l\'inscription';
          }
        }
      });
    } else {
      this.errorMessage = 'Veuillez remplir tous les champs correctement';
    }
  }

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }
}
