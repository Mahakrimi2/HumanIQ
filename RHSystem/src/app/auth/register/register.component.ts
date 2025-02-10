import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { RegisterModel } from 'src/app/models/register.model';
 // Importation du modèle

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
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
      fullname: ['', Validators.required],
      address: ['', Validators.required],
      gender: ['', Validators.required],
      position: ['', Validators.required],
      telNumber: ['', [Validators.required, Validators.pattern('[0-9]{8}')]], // Assuming Tunisian numbers
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit(): void {}

  onSubmit() {
    if (this.registerForm.valid) {
      this.loading = true;
      this.errorMessage = '';

      const formValue = this.registerForm.value;
      const registerData = new RegisterModel(
        formValue.fullname,
        formValue.address,
        formValue.gender,
        formValue.position,
        formValue.telNumber,
        formValue.username,
        formValue.password
      );

      console.log("Tentative d'inscription:", registerData);

      this.authService.register(registerData).subscribe({
        next: (response) => {
          console.log('InscregisterDataription réussie');
          this.loading = false;
          this.router.navigate(['/auth/login']);
        },
        error: (error) => {
          console.error("Erreur d'inscription:", error);
          this.loading = false;
          this.errorMessage = error.error || "Erreur lors de l'inscription";
        },
      });
    } else {
      this.errorMessage = 'Veuillez remplir tous les champs correctement';
    }
  }

  goToLogin() {
    this.router.navigate(['/auth/login']);
  }
}
