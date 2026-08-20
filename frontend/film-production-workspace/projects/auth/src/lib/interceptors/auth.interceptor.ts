import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { API_CONFIG } from '@film-production/core';

import { AuthState } from '../state';

const PUBLIC_AUTH_ENDPOINTS = [
  '/api/v1/auth/register',
  '/api/v1/auth/login',
  '/api/v1/auth/refresh',
  '/api/v1/auth/logout',
  '/api/v1/auth/verify',
  '/api/v1/auth/forgot-password',
  '/api/v1/auth/reset-password',
] as const;

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authState = inject(AuthState);
  const apiConfig = inject(API_CONFIG);
  const gateway = apiConfig.gateway.replace(/\/$/, '');
  const requestUrl = request.urlWithParams;

  if (!isGatewayRequest(requestUrl, gateway) || isPublicAuthRequest(requestUrl, gateway)) {
    return next(request);
  }

  const accessToken = authState.accessToken();

  if (accessToken === null) {
    return next(request);
  }

  return next(
    request.clone({
      setHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),
  );
};

function isGatewayRequest(requestUrl: string, gateway: string): boolean {
  return requestUrl === gateway || requestUrl.startsWith(`${gateway}/`);
}

function isPublicAuthRequest(requestUrl: string, gateway: string): boolean {
  return PUBLIC_AUTH_ENDPOINTS.some((endpoint) => {
    const endpointUrl = `${gateway}${endpoint}`;

    return requestUrl === endpointUrl || requestUrl.startsWith(`${endpointUrl}?`);
  });
}
