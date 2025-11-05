import { Component } from '@angular/core';
import { HeaderComponent } from './header.component';
import { HeroSectionComponent } from './hero-section.component';
import { AboutUsSectionComponent } from './about-us-section.component';
import { ServicesSectionComponent } from './services-section.component';
import { AiSectionComponent } from './ai-section.component';
import { ContactSectionComponent } from './contact-section.component';
import { FooterComponent } from './footer.component';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [
    HeaderComponent,
    HeroSectionComponent,
    AboutUsSectionComponent,
    ServicesSectionComponent,
    AiSectionComponent,
    ContactSectionComponent,
    FooterComponent
  ],
  template: `
    <div class="min-h-screen bg-gray-100 flex flex-col">
      <app-header></app-header>
      <main class="flex-grow">
        <app-hero-section></app-hero-section>
        <app-about-us-section></app-about-us-section>
        <app-services-section></app-services-section>
        <app-ai-section></app-ai-section>
        <app-contact-section></app-contact-section>
      </main>
      <app-footer></app-footer>
    </div>
  `
})
export class LandingPageComponent {}
