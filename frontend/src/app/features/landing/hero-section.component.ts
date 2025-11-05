import { Component } from '@angular/core';
import { NgStyle } from '@angular/common';

@Component({
  selector: 'app-hero-section',
  standalone: true,
  imports: [NgStyle],
  template: `
    <section
      id="inicio"
      class="min-h-screen flex items-center justify-center text-center pt-20"
      [ngStyle]="{
        'background-image': 'url(/assets/hero_background.jpg)',
        'background-size': 'cover',
        'background-position': 'center',
        'background-attachment': 'fixed'
      }"
    >
      <div class="bg-white bg-opacity-80 p-12 rounded-xl shadow-2xl max-w-4xl transform hover:scale-105 transition-transform duration-500">
        <h1 class="text-6xl md:text-8xl font-serif font-extrabold mb-6 text-purple-900 leading-tight drop-shadow-md">
          El Arte de Ser Tú
        </h1>
        <p class="text-xl md:text-2xl mb-10 font-light tracking-wide text-gray-800">
          Descubre un mundo de belleza y bienestar diseñado exclusivamente para ti en Monarca.
        </p>
        <a
          href="#services"
          class="inline-block px-10 py-4 bg-purple-700 text-white font-bold rounded-full text-lg shadow-lg hover:bg-purple-800 transform hover:-translate-y-1 transition duration-300"
        >
          Explora Nuestros Servicios
        </a>
      </div>
    </section>
  `
})
export class HeroSectionComponent {}
