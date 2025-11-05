import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div [class]="containerClass">
      <div class="animate-spin rounded-full border-t-2 border-b-2" 
           [ngClass]="sizeClass"
           [style.border-color]="color">
      </div>
      <p *ngIf="message" [class]="textClass">{{ message }}</p>
    </div>
  `,
  styles: [`
    @keyframes spin {
      to { transform: rotate(360deg); }
    }
    .animate-spin {
      animation: spin 1s linear infinite;
    }
  `]
})
export class LoadingSpinnerComponent {
  @Input() size: 'small' | 'medium' | 'large' = 'medium';
  @Input() color: string = '#ec4899'; // pink-600
  @Input() message?: string;
  @Input() fullScreen: boolean = false;

  get containerClass(): string {
    const base = 'flex flex-col items-center justify-center space-y-2';
    return this.fullScreen 
      ? `${base} fixed inset-0 bg-white bg-opacity-90 z-50` 
      : base;
  }

  get sizeClass(): string {
    const sizes = {
      small: 'h-6 w-6',
      medium: 'h-12 w-12',
      large: 'h-16 w-16'
    };
    return sizes[this.size];
  }

  get textClass(): string {
    return `mt-2 text-gray-600 ${this.size === 'small' ? 'text-sm' : 'text-base'}`;
  }
}
