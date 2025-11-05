import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div *ngIf="isOpen" class="fixed inset-0 z-50 overflow-y-auto" (click)="onBackdropClick($event)">
      <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:block sm:p-0">
        <!-- Background overlay -->
        <div class="fixed inset-0 transition-opacity bg-gray-500 bg-opacity-75" (click)="close()"></div>

        <!-- Center modal -->
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>

        <!-- Modal panel -->
        <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6"
             (click)="$event.stopPropagation()">
          
          <!-- Header -->
          <div class="flex items-center justify-between mb-4" *ngIf="title">
            <h3 class="text-lg font-medium leading-6 text-gray-900">{{ title }}</h3>
            <button
              type="button"
              (click)="close()"
              class="text-gray-400 hover:text-gray-500 focus:outline-none cursor-pointer"
            >
              <svg class="h-6 w-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <!-- Content -->
          <div class="mt-2">
            <ng-content></ng-content>
          </div>

          <!-- Footer Actions -->
          <div class="mt-5 sm:mt-6 flex space-x-3 justify-end" *ngIf="showActions">
            <button
              type="button"
              (click)="close()"
              class="cursor-pointer inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-pink-500 sm:text-sm"
            >
              {{ cancelText }}
            </button>
            <button
              type="button"
              (click)="confirm()"
              [disabled]="confirmDisabled"
              class="cursor-pointer inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-pink-600 text-base font-medium text-white hover:bg-pink-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-pink-500 sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
              [ngClass]="confirmClass"
            >
              {{ confirmText }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: []
})
export class ModalComponent {
  @Input() isOpen: boolean = false;
  @Input() title?: string;
  @Input() showActions: boolean = true;
  @Input() cancelText: string = 'Cancelar';
  @Input() confirmText: string = 'Confirmar';
  @Input() confirmDisabled: boolean = false;
  @Input() confirmClass: string = '';
  
  @Output() closed = new EventEmitter<void>();
  @Output() confirmed = new EventEmitter<void>();

  close(): void {
    this.closed.emit();
  }

  confirm(): void {
    this.confirmed.emit();
  }

  onBackdropClick(event: MouseEvent): void {
    // Close modal when clicking outside
    if ((event.target as HTMLElement).classList.contains('overflow-y-auto')) {
      this.close();
    }
  }
}
