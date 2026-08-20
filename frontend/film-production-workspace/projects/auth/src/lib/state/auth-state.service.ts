import { Injectable, Signal, computed, inject, signal } from '@angular/core';

import { AuthUser, TokenResponse } from '../models';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthState {
  private readonly tokenStorage = inject(TokenStorageService);

  private readonly userState = signal<AuthUser | null>(null);
  private readonly accessTokenState = signal<string | null>(null);
  private readonly refreshTokenState = signal<string | null>(null);
  private readonly initializedState = signal(false);
  private readonly refreshingState = signal(false);

  readonly user: Signal<AuthUser | null> = this.userState.asReadonly();
  readonly accessToken: Signal<string | null> = this.accessTokenState.asReadonly();
  readonly refreshToken: Signal<string | null> = this.refreshTokenState.asReadonly();
  readonly isInitialized: Signal<boolean> = this.initializedState.asReadonly();
  readonly isRefreshing: Signal<boolean> = this.refreshingState.asReadonly();

  readonly isAuthenticated = computed(() => this.accessTokenState() !== null);
  readonly isGuest = computed(() => !this.isAuthenticated());

  hydrate(): void {
    this.accessTokenState.set(this.tokenStorage.getAccessToken());
    this.refreshTokenState.set(this.tokenStorage.getRefreshToken());
    this.userState.set(this.tokenStorage.getCurrentUser());
    this.initializedState.set(true);
  }

  setSession(tokens: TokenResponse, user: AuthUser): void {
    this.tokenStorage.setSession(tokens, user);
    this.accessTokenState.set(tokens.accessToken);
    this.refreshTokenState.set(tokens.refreshToken);
    this.userState.set(user);
  }

  setTokens(tokens: TokenResponse): void {
    this.tokenStorage.setTokens(tokens);
    this.accessTokenState.set(tokens.accessToken);
    this.refreshTokenState.set(tokens.refreshToken);
  }

  setUser(user: AuthUser): void {
    this.tokenStorage.setCurrentUser(user);
    this.userState.set(user);
  }

  clearSession(): void {
    this.tokenStorage.clearSession();
    this.accessTokenState.set(null);
    this.refreshTokenState.set(null);
    this.userState.set(null);
  }

  setRefreshing(value: boolean): void {
    this.refreshingState.set(value);
  }
}
