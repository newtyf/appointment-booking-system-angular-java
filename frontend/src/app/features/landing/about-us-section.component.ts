import { Component } from '@angular/core';

@Component({
  selector: 'app-about-us-section',
  standalone: true,
  template: `
    <section id="about" class="py-24 bg-gradient-to-br from-gray-50 to-purple-50 text-center relative overflow-hidden">
      <div class="container mx-auto px-6">
        <h2 class="text-5xl font-serif text-purple-800 mb-12">
          Nuestra Filosofía
        </h2>
        <div class="flex flex-col md:flex-row items-center justify-center gap-12">
          <div class="md:w-1/2 text-left">
            <p class="text-xl text-gray-700 leading-relaxed mb-6">
              En Monarca, creemos que la belleza va más allá de la superficie. Es un reflejo de equilibrio, paz y cuidado personal. Nos dedicamos a crear un santuario donde cada visita es una oportunidad para rejuvenecer cuerpo y alma.
            </p>
            <p class="text-xl text-gray-700 leading-relaxed">
              Nuestro equipo de especialistas, apasionados por el bienestar, utiliza técnicas avanzadas y productos de la más alta calidad para ofrecerte una experiencia personalizada y resultados excepcionales que realzan tu esencia natural.
            </p>
          </div>
          <div class="md:w-1/2 flex justify-center">
            <img 
              src="/assets/about_us_spa_interior.jpg" 
              alt="Interior elegante de Monarca Spa" 
              class="rounded-xl shadow-2xl transform hover:scale-105 transition-transform duration-500 max-w-full h-auto object-cover w-full md:h-96"
            />
          </div>
        </div>
      </div>
    </section>
  `
})
export class AboutUsSectionComponent {}
