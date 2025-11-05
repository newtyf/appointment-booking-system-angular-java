import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { LoginRequest } from '../../../shared/models/user.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  credentials: LoginRequest = {
    email: '',
    password: ''
  };
  
  errorMessage = '';
  successMessage = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onSubmit(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.login(this.credentials).subscribe({
      next: (response) => {
        this.successMessage = '¡Inicio de sesión exitoso!';
        
        // Redirect based on role after 1 second
        setTimeout(() => {
          const user = response.user;
          if (user.role === 'admin') {
            this.router.navigate(['/admin']);
          } else if (user.role === 'receptionist') {
            this.router.navigate(['/receptionist']);
          } else if (user.role === 'stylist') {
            this.router.navigate(['/stylist']);
          } else {
            this.router.navigate(['/client']);
          }
        }, 1000);
      },
      error: (error) => {
        if (error.error?.detail) {
          this.errorMessage = error.error.detail;
        } else if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend esté funcionando.';
        } else {
          this.errorMessage = 'Credenciales incorrectas. Inténtalo de nuevo.';
        }
        this.loading = false;
      }
    });
  }
}
