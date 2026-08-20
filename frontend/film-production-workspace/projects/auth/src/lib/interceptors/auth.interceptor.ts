import { HttpContextToken, HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { API_CONFIG } from '@film-production/core';
import { catchError, switchMap, throwError } from 'rxjs';

import { TokenRefreshCoordinatorService } from '../services/token-refresh-coordinator.service';
import { AuthState } from '../state/auth-state.service';

const PUBLIC_AUTH_ENDPOINTS = [
  '/api/v1/auth/register',
  '/api/v1/auth/login',
  '/api/v1/auth/refresh',
  '/api/v1/auth/logout',
  '/api/v1/auth/verify',
  '/api/v1/auth/forgot-password',
  '/api/v1/auth/reset-password',
] as const;

const AUTH_RETRY_ATTEMPTED = new HttpContextToken<boolean>(() => false);

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authState = inject(AuthState);
  const tokenRefreshCoordinator = inject(TokenRefreshCoordinatorService);
  const apiConfig = inject(API_CONFIG);
  const gateway = apiConfig.gateway.replace(/\/$/, '');
  const requestUrl = request.urlWithParams;

  if (!isGatewayRequest(requestUrl, gateway) || isPublicAuthRequest(requestUrl, gateway)) {
    return next(request);
  }

  const accessToken = authState.accessToken();
  const authenticatedRequest = accessToken === null
    ? request
    : request.clone({
        setHeaders: {
          Authorization: `Bearer ${accessToken}`,
        },
      });

  return next(authenticatedRequest).pipe(
    catchError((error: unknown) => {
      if (!shouldRefresh(error, request)) {
        return throwError(() => error);
      }

      if (authState.refreshToken() === null) {
        authState.clearSession();
        return throwError(() => error);
      }

      return tokenRefreshCoordinator.refresh().pipe(
        switchMap(() => retryWithRefreshedToken(request, next, authState)),
      );
    }),
  );
};

function shouldRefresh(error: unknown, request: Parameters<HttpInterceptorFn>[0]): boolean {
  return error instanceof HttpErrorResponse
    && error.status === 401
    && !request.context.get(AUTH_RETRY_ATTEMPTED);
}

function retryWithRefreshedToken(
  request: Parameters<HttpInterceptorFn>[0],
  next: Parameters<HttpInterceptorFn>[1],
  authState: AuthState,
) {
  const accessToken = authState.accessToken();

  if (accessToken === null) {
    authState.clearSession();
    return throwError(() => new Error('Token refresh did not provide an access token.'));
  }

  return next(
    request.clone({
      context: request.context.set(AUTH_RETRY_ATTEMPTED, true),
      setHeaders: {
        Authorization: `Bearer ${accessToken}`,
      },
    }),
  );
}

function isGatewayRequest(requestUrl: string, gateway: string): boolean {
  return requestUrl === gateway || requestUrl.startsWith(`${gateway}/`);
}

function isPublicAuthRequest(requestUrl: string, gateway: string): boolean {
  return PUBLIC_AUTH_ENDPOINTS.some((endpoint) => {
    const endpointUrl = `${gateway}${endpoint}`;

    return requestUrl === endpointUrl || requestUrl.startsWith(`${endpointUrl}?`);
  });
}
