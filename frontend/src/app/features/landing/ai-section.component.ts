import { Component } from '@angular/core';
import { ImageUploadComponent } from './image-upload.component';

@Component({
  selector: 'app-ai-section',
  standalone: true,
  imports: [ImageUploadComponent],
  template: `
    <section id="ia" class="py-24 bg-gradient-to-tr from-pink-100 to-purple-100 text-center">
      <div class="container mx-auto px-6">
        <h2 class="text-5xl font-serif text-purple-800 mb-12">
          Tu Asesor Personal de Belleza IA
        </h2>
        <p class="text-xl text-gray-700 mb-10 max-w-3xl mx-auto">
          ¿Tienes dudas sobre tu rutina de belleza o el mejor tratamiento para ti? ¡Nuestra Inteligencia Artificial está aquí para ayudarte! Obtén recomendaciones personalizadas y resuelve tus preguntas al instante.
        </p>
        <div class="bg-white p-10 rounded-2xl shadow-2xl mx-auto border border-purple-200">
          <app-image-upload></app-image-upload>
        </div>
      </div>
    </section>
  `
})
export class AiSectionComponent {}
