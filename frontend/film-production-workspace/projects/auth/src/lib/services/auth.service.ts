import { Injectable, inject } from '@angular/core';
import { finalize, Observable, of, switchMap, tap, throwError } from 'rxjs';

import {
  AuthUser,
  LoginRequest,
  RefreshTokenResponse,
  RegisterRequest,
} from '../models';
import { AuthState } from '../state';
import { AuthApiService } from './auth-api.service';
import { CurrentUserApiService } from './current-user-api.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly authApi = inject(AuthApiService);
  private readonly currentUserApi = inject(CurrentUserApiService);
  private readonly authState = inject(AuthState);

  login(request: LoginRequest): Observable<AuthUser> {
    return this.authApi.login(request).pipe(
      tap((tokens) => this.authState.setTokens(tokens)),
      switchMap(() => this.currentUserApi.getCurrentUser()),
      tap((user) => this.authState.setUser(user)),
    );
  }

  register(request: RegisterRequest): Observable<AuthUser> {
    return this.authApi.register(request);
  }

  refresh(): Observable<RefreshTokenResponse> {
    const refreshToken = this.authState.refreshToken();

    if (refreshToken === null) {
      return throwError(() => new Error('No refresh token is available.'));
    }

    return this.authApi.refresh({ refreshToken }).pipe(
      tap((tokens) => this.authState.setTokens(tokens)),
    );
  }

  logout(): Observable<void> {
    const refreshToken = this.authState.refreshToken();

    if (refreshToken === null) {
      this.authState.clearSession();
      return of(undefined);
    }

    return this.authApi.logout({ refreshToken }).pipe(
      finalize(() => this.authState.clearSession()),
    );
  }

  hydrate(): void {
    this.authState.hydrate();
  }
}
