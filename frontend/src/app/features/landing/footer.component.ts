import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  template: `
    <footer class="bg-gray-900 text-white py-10 text-center">
      <div class="container mx-auto px-6">
        <p class="text-lg mb-4">&copy; {{ currentYear }} Monarca. Todos los derechos reservados.</p>
        <div class="flex justify-center space-x-6 mb-4">
          <a href="#" class="text-gray-400 hover:text-white transition duration-300">Facebook</a>
          <a href="#" class="text-gray-400 hover:text-white transition duration-300">Instagram</a>
          <a href="#" class="text-gray-400 hover:text-white transition duration-300">TikTok</a>
        </div>
        <div class="text-sm text-gray-500">
          <a href="#" class="hover:text-white mx-2">Política de Privacidad</a> |
          <a href="#" class="hover:text-white mx-2">Términos de Servicio</a>
        </div>
      </div>
    </footer>
  `
})
export class FooterComponent {
  currentYear = new Date().getFullYear();
}
