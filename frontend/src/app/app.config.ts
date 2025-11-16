import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { BeneficioHttpRepository } from './data/repositories/beneficio-http.repository';
import { BeneficioRepository } from '../domain/repositories/beneficio.repository';
import { provideHttpClient } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    {
      provide: BeneficioRepository,
      useClass: BeneficioHttpRepository
    }
  ]
};
