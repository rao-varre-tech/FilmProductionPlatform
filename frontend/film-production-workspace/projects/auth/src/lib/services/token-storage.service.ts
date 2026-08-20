import { Injectable, inject } from '@angular/core';
import { StorageService } from '@film-production/core';

import { AuthUser, TokenResponse } from '../models';

@Injectable({
  providedIn: 'root',
})
export class TokenStorageService {
  private readonly storage = inject(StorageService);

  private readonly accessTokenKey = 'access_token';
  private readonly refreshTokenKey = 'refresh_token';
  private readonly currentUserKey = 'current_user';

  getAccessToken(): string | null {
    return this.storage.get<string>(this.accessTokenKey);
  }

  getRefreshToken(): string | null {
    return this.storage.get<string>(this.refreshTokenKey);
  }

  getCurrentUser(): AuthUser | null {
    return this.storage.get<AuthUser>(this.currentUserKey);
  }

  setTokens(tokens: TokenResponse): void {
    this.storage.set<string>(this.accessTokenKey, tokens.accessToken);
    this.storage.set<string>(this.refreshTokenKey, tokens.refreshToken);
  }

  setCurrentUser(user: AuthUser): void {
    this.storage.set<AuthUser>(this.currentUserKey, user);
  }

  setSession(tokens: TokenResponse, user: AuthUser): void {
    this.setTokens(tokens);
    this.setCurrentUser(user);
  }

  clearTokens(): void {
    this.storage.remove(this.accessTokenKey);
    this.storage.remove(this.refreshTokenKey);
  }

  clearSession(): void {
    this.clearTokens();
    this.storage.remove(this.currentUserKey);
  }

  hasAccessToken(): boolean {
    return this.storage.has(this.accessTokenKey);
  }
}
