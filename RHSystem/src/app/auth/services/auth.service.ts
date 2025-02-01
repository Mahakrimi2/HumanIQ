import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { LoginDTO, AuthRequest } from '../../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8082/api/auth';

  constructor(private http: HttpClient) { }

  register(registerData: { email: string; password: string; }): Observable<any> {
    console.log('Envoi de la requête d\'inscription:', registerData);
    return this.http.post<any>(`${this.apiUrl}/register`, { email: registerData.email, password: registerData.password , role:"Employee" })
      .pipe(
        catchError(this.handleError)
      );
  }

  login(authRequest: AuthRequest): Observable<LoginDTO> {
    console.log('Envoi de la requête de connexion:', authRequest);
    return this.http.post<LoginDTO>(`${this.apiUrl}/login`, authRequest)
      .pipe(
        tap((response: LoginDTO) => {
          console.log('Réponse reçue:', response);
          if (response && response.token) {
            localStorage.setItem('token', response.token);
          }
        }),
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('Une erreur s\'est produite:', error);
    let errorMessage = 'Une erreur inconnue est survenue.';

    if (error.status === 0) {
      errorMessage = 'Erreur de connexion au serveur.';
    } else if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = `Erreur: ${error.error.message}`;
    } else {
      // Erreur côté serveur
      errorMessage = `Erreur ${error.status}: ${error.message}`;
    }

    return throwError(errorMessage);
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }
}
