import { Injectable, inject } from '@angular/core';
import { catchError, finalize, Observable, shareReplay, throwError } from 'rxjs';

import { RefreshTokenResponse } from '../models';
import { AuthState } from '../state/auth-state.service';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class TokenRefreshCoordinatorService {
  private readonly authService = inject(AuthService);
  private readonly authState = inject(AuthState);

  private refreshOperation: Observable<RefreshTokenResponse> | null = null;

  refresh(): Observable<RefreshTokenResponse> {
    if (this.refreshOperation !== null) {
      return this.refreshOperation;
    }

    if (this.authState.refreshToken() === null) {
      this.authState.clearSession();
      return throwError(() => new Error('No refresh token is available.'));
    }

    this.authState.setRefreshing(true);

    this.refreshOperation = this.authService.refresh().pipe(
      catchError((error: unknown) => {
        this.authState.clearSession();
        return throwError(() => error);
      }),
      finalize(() => {
        this.refreshOperation = null;
        this.authState.setRefreshing(false);
      }),
      shareReplay({ bufferSize: 1, refCount: false }),
    );

    return this.refreshOperation;
  }
}
