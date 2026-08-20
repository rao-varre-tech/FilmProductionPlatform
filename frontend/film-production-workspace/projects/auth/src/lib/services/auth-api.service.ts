import { Injectable, inject } from '@angular/core';
import { API_CONFIG, ApiService, HttpService } from '@film-production/core';
import { map, Observable } from 'rxjs';

import {
  ApiResponse,
  AuthUser,
  ForgotPasswordRequest,
  LoginRequest,
  LoginResponse,
  LogoutRequest,
  RefreshTokenRequest,
  RefreshTokenResponse,
  RegisterRequest,
  ResetPasswordRequest,
} from '../models';

@Injectable({
  providedIn: 'root',
})
export class AuthApiService {
  private readonly api = inject(ApiService);
  private readonly http = inject(HttpService);
  private readonly apiConfig = inject(API_CONFIG);

  private readonly basePath = '/api/v1/auth';

  register(request: RegisterRequest): Observable<AuthUser> {
    return this.api
      .post<RegisterRequest, ApiResponse<AuthUser>>(`${this.basePath}/register`, request)
      .pipe(map((response) => this.unwrap(response, 'register')));
  }

  login(request: LoginRequest): Observable<LoginResponse> {
    return this.api
      .post<LoginRequest, ApiResponse<LoginResponse>>(`${this.basePath}/login`, request)
      .pipe(map((response) => this.unwrap(response, 'login')));
  }

  refresh(request: RefreshTokenRequest): Observable<RefreshTokenResponse> {
    return this.api
      .post<RefreshTokenRequest, ApiResponse<RefreshTokenResponse>>(
        `${this.basePath}/refresh`,
        request,
      )
      .pipe(map((response) => this.unwrap(response, 'refresh token')));
  }

  logout(request: LogoutRequest): Observable<void> {
    return this.api
      .post<LogoutRequest, ApiResponse<void>>(`${this.basePath}/logout`, request)
      .pipe(map(() => undefined));
  }

  verifyEmail(token: string): Observable<void> {
    const url = `${this.buildUrl(`${this.basePath}/verify`)}?token=${encodeURIComponent(token)}`;

    return this.http.get<ApiResponse<void>>(url).pipe(map(() => undefined));
  }

  forgotPassword(request: ForgotPasswordRequest): Observable<void> {
    return this.api
      .post<ForgotPasswordRequest, ApiResponse<void>>(
        `${this.basePath}/forgot-password`,
        request,
      )
      .pipe(map(() => undefined));
  }

  resetPassword(request: ResetPasswordRequest): Observable<void> {
    return this.api
      .post<ResetPasswordRequest, ApiResponse<void>>(
        `${this.basePath}/reset-password`,
        request,
      )
      .pipe(map(() => undefined));
  }

  private unwrap<T>(response: ApiResponse<T>, operation: string): T {
    if (response.data === null) {
      throw new Error(`The ${operation} response did not include data.`);
    }

    return response.data;
  }

  private buildUrl(endpoint: string): string {
    const gateway = this.apiConfig.gateway.replace(/\/$/, '');

    return `${gateway}${endpoint}`;
  }
}
