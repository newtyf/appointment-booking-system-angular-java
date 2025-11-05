import { Component, signal } from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

interface HairColor {
  id: string;
  label: string;
  promptValue: string;
}

@Component({
  selector: 'app-image-upload',
  standalone: true,
  imports: [NgFor, NgIf, FormsModule],
  template: `
    <div class="min-h-screen bg-gradient-to-br from-purple-100 to-blue-200 flex items-center justify-center p-4 font-sans">
      <div class="bg-white p-8 rounded-2xl shadow-xl max-w-4xl w-full flex flex-col md:flex-row gap-8 items-center justify-center">
        <!-- Left Section: Image Upload and Preview -->
        <div class="flex-2 w-full flex flex-col items-center">
          <h2 class="text-3xl font-bold text-gray-800 mb-6 text-center">
            Editor de Color de Cabello
          </h2>

          <label
            for="image-upload"
            class="cursor-pointer bg-gradient-to-r from-purple-500 to-blue-500 text-white px-6 py-3 rounded-full font-semibold hover:from-purple-600 hover:to-blue-600 transition duration-300 shadow-lg mb-6 inline-flex items-center"
          >
            <svg class="w-6 h-6 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12"></path>
            </svg>
            Seleccionar Imagen
          </label>
          <input
            id="image-upload"
            type="file"
            accept="image/*"
            (change)="onFileChange($event)"
            class="hidden"
          />

          <!-- Image Preview -->
          <div *ngIf="imagePreviewUrl()" class="w-full max-w-md border-4 border-purple-300 rounded-lg overflow-hidden shadow-lg mb-4">
            <img [src]="imagePreviewUrl()" alt="Preview" class="w-full h-auto object-cover" />
          </div>

          <!-- Processed Image Display -->
          <div *ngIf="processedImageUrl()" class="w-full max-w-md border-4 border-green-400 rounded-lg overflow-hidden shadow-lg mb-4">
            <img [src]="processedImageUrl()" alt="Processed" class="w-full h-auto object-cover" />
            <p class="text-center text-green-600 font-semibold mt-2">¡Imagen Procesada!</p>
          </div>

          <!-- Loader -->
          <div *ngIf="isLoading()" class="text-center">
            <div class="inline-block w-12 h-12 border-4 border-purple-500 border-t-transparent rounded-full animate-spin"></div>
            <p class="text-gray-700 mt-4">Procesando imagen...</p>
          </div>

          <!-- Error Message -->
          <div *ngIf="errorMessage()" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mt-4 w-full max-w-md">
            <strong class="font-bold">Error:</strong>
            <span class="block sm:inline"> {{ errorMessage() }}</span>
          </div>
        </div>

        <!-- Right Section: Hair Color Selection -->
        <div class="flex-1 w-full flex flex-col items-center">
          <h3 class="text-2xl font-semibold text-gray-700 mb-6">Selecciona un Color</h3>
          <div class="space-y-4 w-full">
            <div *ngFor="let color of hairColors" class="flex items-center">
              <input
                [id]="'color-' + color.id"
                type="radio"
                name="hairColor"
                [value]="color.promptValue"
                [(ngModel)]="selectedHairColor"
                class="w-5 h-5 text-purple-600 focus:ring-purple-500"
              />
              <label [for]="'color-' + color.id" class="ml-3 text-lg text-gray-800 cursor-pointer">
                {{ color.label }}
              </label>
            </div>
          </div>

          <button
            (click)="handleSubmit()"
            [disabled]="isLoading()"
            class="mt-8 bg-gradient-to-r from-pink-500 to-purple-500 text-white px-8 py-3 rounded-full font-bold hover:from-pink-600 hover:to-purple-600 transition duration-300 shadow-lg disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {{ isLoading() ? 'Procesando...' : 'Transformar Cabello' }}
          </button>
        </div>
      </div>
    </div>
  `
})
export class ImageUploadComponent {
  selectedFile: File | null = null;
  imagePreviewUrl = signal<string | null>(null);
  processedImageUrl = signal<string | null>(null);
  selectedHairColor = '';
  isLoading = signal(false);
  errorMessage = signal('');

  hairColors: HairColor[] = [
    { id: "black", label: "Negro", promptValue: "black" },
    { id: "blonde", label: "Rubio", promptValue: "blonde" },
    { id: "brown", label: "Castaño", promptValue: "brown" },
    { id: "red", label: "Rojo", promptValue: "red" },
    { id: "blue", label: "Azul", promptValue: "blue" },
  ];

  constructor(private http: HttpClient) {}

  onFileChange(event: Event): void {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];
    
    if (!file) {
      this.imagePreviewUrl.set(null);
      return;
    }

    this.selectedFile = file;
    this.processedImageUrl.set(null);
    this.errorMessage.set('');

    const reader = new FileReader();
    reader.onloadend = () => {
      this.imagePreviewUrl.set(reader.result as string);
    };
    reader.readAsDataURL(file);
  }

  handleSubmit(): void {
    if (!this.selectedFile) {
      this.errorMessage.set('Por favor, selecciona una imagen primero.');
      return;
    }
    if (!this.selectedHairColor) {
      this.errorMessage.set('Por favor, selecciona un color de cabello.');
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    const formData = new FormData();
    formData.append('file', this.selectedFile);
    formData.append('prompt', `change hair color to ${this.selectedHairColor}`);

    this.http.post<{ image_url: string }>('/api/ai/process-image', formData)
      .subscribe({
        next: (data) => {
          this.processedImageUrl.set(data.image_url);
          this.imagePreviewUrl.set(null);
          this.isLoading.set(false);
        },
        error: (error) => {
          console.error('Error al enviar la imagen:', error);
          this.errorMessage.set(`Error: ${error.error?.detail || 'Error al procesar la imagen.'}`);
          this.isLoading.set(false);
        }
      });
  }
}
