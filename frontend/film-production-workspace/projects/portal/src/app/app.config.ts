import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';

import { provideRouter } from '@angular/router';
import { provideAuth } from '@film-production/auth';
import { API_CONFIG, provideCore } from '@film-production/core';
import { environment } from '../environments/environment';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideCore(),
    provideAuth(),
    {
      provide: API_CONFIG,
      useValue: {
        gateway: environment.api.gateway,
        version: environment.api.version,
        timeout: environment.api.timeout
      }
    }
  ]
};
