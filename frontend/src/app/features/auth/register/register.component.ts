import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { RegisterRequest } from '../../../shared/models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  form: RegisterRequest = {
    name: '',
    email: '',
    phone: '',
    password: ''
  };
  
  confirmPassword = '';
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

    if (this.form.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden.';
      this.loading = false;
      return;
    }

    this.authService.register(this.form).subscribe({
      next: () => {
        this.successMessage = '¡Registro exitoso! Ahora puedes iniciar sesión.';
        this.form = { name: '', email: '', phone: '', password: '' };
        this.confirmPassword = '';
        
        setTimeout(() => {
          this.router.navigate(['/auth/login']);
        }, 2000);
      },
      error: (error) => {
        if (error.error?.detail) {
          if (Array.isArray(error.error.detail)) {
            // Pydantic validation errors
            this.errorMessage = error.error.detail.map((errItem: any) => {
              const field = errItem.loc && errItem.loc.length > 1 ? errItem.loc[1] : 'campo';
              return `${field}: ${errItem.msg}`;
            }).join('; ');
          } else {
            this.errorMessage = error.error.detail;
          }
        } else if (error.status === 0) {
          this.errorMessage = 'No se pudo conectar con el servidor. Verifica que el backend esté funcionando.';
        } else {
          this.errorMessage = 'Error en el registro. Inténtalo de nuevo.';
        }
        this.loading = false;
      }
    });
  }
}
