import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  template: `
    <header class="bg-white shadow-lg py-4 fixed w-full z-50 top-0">
      <nav class="container mx-auto flex justify-between items-center px-4">
        <div class="flex items-center">
          <a href="#inicio">
            <img src="/assets/monarcaLogo.png" alt="Monarca Logo" class="h-12 w-auto" />
            <span class="text-3xl font-serif text-purple-800 tracking-wide">Monarca</span>
          </a>
        </div>

        <ul class="flex space-x-8">
          <li><a href="#inicio" class="text-gray-700 hover:text-pink-600 transition duration-300 font-medium">Inicio</a></li>
          <li><a href="#about" class="text-gray-700 hover:text-pink-600 transition duration-300 font-medium">Nosotros</a></li>
          <li><a href="#services" class="text-gray-700 hover:text-pink-600 transition duration-300 font-medium">Servicios</a></li>
          <li><a href="#ia" class="text-gray-700 hover:text-pink-600 transition duration-300 font-medium">Asesor IA</a></li>
          <li><a href="#contact" class="text-gray-700 hover:text-pink-600 transition duration-300 font-medium">Contacto</a></li>
        </ul>

        <div class="space-x-4">
          <a
            routerLink="/auth/login"
            class="px-6 py-2 border-2 border-purple-600 text-purple-600 rounded-full hover:bg-purple-600 hover:text-white transition duration-300 font-semibold"
          >
            Iniciar Sesión
          </a>
          <a
            routerLink="/auth/register"
            class="px-6 py-2 bg-pink-600 text-white rounded-full hover:bg-pink-700 transition duration-300 font-semibold"
          >
            Registrarse
          </a>
        </div>
      </nav>
    </header>
  `
})
export class HeaderComponent {}
