import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact-section',
  standalone: true,
  imports: [FormsModule],
  template: `
    <section id="contact" class="py-24 bg-white text-center">
      <div class="container mx-auto px-6">
        <h2 class="text-5xl font-serif text-purple-800 mb-12">
          Hablemos de Tu Belleza
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-12 items-start">
          <div class="text-left bg-purple-50 p-8 rounded-xl shadow-lg border border-purple-100">
            <h3 class="text-3xl font-semibold text-purple-700 mb-6">Encuéntranos Aquí</h3>
            <p class="text-lg text-gray-700 mb-4 flex items-center">
              <i class="fas fa-envelope mr-3 text-pink-600"></i> 
              <span class="font-bold">Email:</span> contacto&#64;monarca.com
            </p>
            <p class="text-lg text-gray-700 mb-4 flex items-center">
              <i class="fas fa-phone-alt mr-3 text-pink-600"></i> 
              <span class="font-bold">Teléfono:</span> +51 999 999 999
            </p>
            <p class="text-lg text-gray-700 mb-4 flex items-center">
              <i class="fas fa-map-marker-alt mr-3 text-pink-600"></i> 
              <span class="font-bold">Dirección:</span> Av. Principal 123, Miraflores, Lima, Perú
            </p>
            <p class="text-lg text-gray-700 mb-6 flex items-center">
              <i class="fas fa-clock mr-3 text-pink-600"></i> 
              <span class="font-bold">Horario:</span> Lun-Vie: 9:00 - 19:00 | Sáb: 10:00 - 17:00
            </p>
            
            <!-- Google Maps -->
            <div class="h-64 bg-gray-200 rounded-lg mt-6 overflow-hidden border border-gray-300">
              <iframe 
                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3900.569992345479!2d-77.01977799999999!3d-12.158525099999999!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105b8b8b8b8b8b8%3A0x92f6c99c89c8d8b8!2sTu%20Ubicaci%C3%B3n%20Exacta!5e0!3m2!1ses-419!2spe!4v1678901234567" 
                width="100%" 
                height="100%" 
                style="border: 0;" 
                allowfullscreen 
                loading="lazy" 
                referrerpolicy="no-referrer-when-downgrade"
                title="Ubicación de Monarca Salón"
              ></iframe>
            </div>
          </div>

          <div class="text-left bg-white p-8 rounded-xl shadow-lg border border-pink-100">
            <h3 class="text-3xl font-semibold text-pink-700 mb-6">Envíanos un Mensaje</h3>
            <img src="/assets/contact_us_hands.jpg" alt="Manos en un spa" class="w-full h-64 object-cover rounded-lg mb-6 shadow-md" />
            <form class="space-y-6">
              <div>
                <label for="name" class="block text-gray-700 text-sm font-bold mb-2">Nombre Completo</label>
                <input 
                  type="text" 
                  id="name" 
                  class="shadow appearance-none border border-gray-300 rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:ring-2 focus:ring-purple-300 transition duration-200" 
                  placeholder="Tu Nombre" 
                />
              </div>
              <div>
                <label for="email" class="block text-gray-700 text-sm font-bold mb-2">Email</label>
                <input 
                  type="email" 
                  id="email" 
                  class="shadow appearance-none border border-gray-300 rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:ring-2 focus:ring-purple-300 transition duration-200" 
                  placeholder="tu.email@ejemplo.com" 
                />
              </div>
              <div>
                <label for="message" class="block text-gray-700 text-sm font-bold mb-2">Mensaje</label>
                <textarea 
                  id="message" 
                  rows="5" 
                  class="shadow appearance-none border border-gray-300 rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:ring-2 focus:ring-purple-300 transition duration-200" 
                  placeholder="Tu Mensaje o Consulta"
                ></textarea>
              </div>
              <button 
                type="submit" 
                class="w-full bg-pink-600 text-white font-bold py-3 px-4 rounded-full hover:bg-pink-700 transition duration-300 shadow-md transform hover:-translate-y-0.5"
              >
                Enviar Mensaje
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>
  `
})
export class ContactSectionComponent {}
