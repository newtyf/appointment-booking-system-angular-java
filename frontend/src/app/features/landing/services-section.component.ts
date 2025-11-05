import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgFor } from '@angular/common';

interface Service {
  title: string;
  description: string;
  image: string;
  alt: string;
}

@Component({
  selector: 'app-services-section',
  standalone: true,
  imports: [RouterLink, NgFor],
  template: `
    <section id="services" class="py-24 bg-gradient-to-bl from-purple-50 to-pink-50 text-center">
      <div class="container mx-auto px-6">
        <h2 class="text-5xl font-serif text-purple-800 mb-12">
          Nuestros Servicios de Lujo
        </h2>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
          <div
            *ngFor="let service of services"
            class="bg-white p-8 rounded-2xl shadow-xl hover:shadow-2xl transform hover:-translate-y-2 transition duration-500 ease-in-out border border-purple-100 flex flex-col items-center"
          >
            <img [src]="service.image" [alt]="service.alt" class="w-full h-48 object-cover rounded-lg mb-6 shadow-md" />
            <h3 class="text-3xl font-semibold text-purple-700 mb-4 font-sans">{{ service.title }}</h3>
            <p class="text-gray-600 leading-relaxed">{{ service.description }}</p>
          </div>
        </div>
        <div class="mt-12">
          <a
            routerLink="/auth/login"
            class="inline-block px-8 py-3 bg-pink-600 text-white font-bold rounded-full text-lg shadow-lg hover:bg-pink-700 transform hover:-translate-y-1 transition duration-300"
          >
            Agenda Tu Cita Ahora
          </a>
        </div>
      </div>
    </section>
  `
})
export class ServicesSectionComponent {
  services: Service[] = [
    {
      title: "Cuidado Capilar Exclusivo",
      description: "Desde cortes de vanguardia y coloraciones vibrantes hasta tratamientos reparadores y peinados para eventos especiales. Utilizamos las mejores marcas para proteger y embellecer tu cabello.",
      image: "/assets/service_hair.jpg",
      alt: "Servicio de Peluquería",
    },
    {
      title: "Rituales Faciales Rejuvenecedores",
      description: "Limpiezas profundas, hidratación intensa, tratamientos antiedad y masajes que devuelven la luminosidad a tu piel. Personalizamos cada sesión para tu tipo de piel.",
      image: "/assets/service_facial.jpg",
      alt: "Servicio de Estética Facial",
    },
    {
      title: "Experiencias de Relajación Spa",
      description: "Masajes terapéuticos, envolturas corporales y circuitos de bienestar diseñados para revitalizar cuerpo y mente. Un escape perfecto para liberarte del estrés diario.",
      image: "/assets/service_spa.jpg",
      alt: "Servicio de Spa y Masajes",
    },
  ];
}
