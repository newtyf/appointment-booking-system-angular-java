import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { CulqiCheckoutConfig, CulqiToken } from '../../shared/models/payment.model';

declare global {
  interface Window {
    Culqi: any;
  }
}

@Injectable({
  providedIn: 'root'
})
export class CulqiService {
  private culqiLoaded = false;

  constructor() {
    this.loadCulqiScript();
  }

  private loadCulqiScript(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.culqiLoaded) {
        resolve();
        return;
      }

      const script = document.createElement('script');
      script.src = 'https://checkout.culqi.com/js/v4';
      script.onload = () => {
        this.culqiLoaded = true;
        resolve();
      };
      script.onerror = () => reject(new Error('Failed to load Culqi script'));
      document.body.appendChild(script);
    });
  }

  async openCheckout(config: CulqiCheckoutConfig): Promise<void> {
    try {
      await this.loadCulqiScript();

      if (!window.Culqi) {
        throw new Error('Culqi is not loaded');
      }

      window.Culqi.publicKey = environment.culqiPublicKey;
      
      window.Culqi.settings({
        title: 'Monarca Beauty Salon',
        currency: config.currency || 'PEN',
        amount: config.amount,
        order: config.description || 'Pago de servicio'
      });

      window.Culqi.options({
        lang: 'es',
        installments: false,
        paymentMethods: {
          tarjeta: true,
          yape: false,
          billetera: false,
          bancaMovil: false,
          agente: false,
          cuotealo: false
        }
      });

      // Override Culqi.open callback
      const originalOpen = window.Culqi.open;
      window.Culqi.open = function() {
        originalOpen.call(window.Culqi);
      };

      // Set success callback (cleanup after use to prevent memory leaks)
      window.culqi = function() {
        try {
          if (window.Culqi.token) {
            const token: CulqiToken = {
              id: window.Culqi.token.id,
              type: window.Culqi.token.type,
              email: window.Culqi.token.email,
              card_number: window.Culqi.token.card_number,
              creation_date: window.Culqi.token.creation_date
            };
            config.onSuccess(token);
          } else if (window.Culqi.error) {
            config.onError(window.Culqi.error);
          }
        } finally {
          // Cleanup global function to prevent memory leaks
          delete window.culqi;
        }
      };

      window.Culqi.open();
    } catch (error) {
      config.onError(error);
      // Cleanup on error as well
      if (window.culqi) {
        delete window.culqi;
      }
    }
  }
}
