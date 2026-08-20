import { Injectable, inject } from '@angular/core';
import { ApiService } from '@film-production/core';
import { map, Observable } from 'rxjs';

import { ApiResponse, AuthUser } from '../models';

@Injectable({
  providedIn: 'root',
})
export class CurrentUserApiService {
  private readonly api = inject(ApiService);

  getCurrentUser(): Observable<AuthUser> {
    return this.api
      .get<ApiResponse<AuthUser>>('/api/v1/users/me')
      .pipe(map((response) => this.unwrap(response)));
  }

  private unwrap(response: ApiResponse<AuthUser>): AuthUser {
    if (response.data === null) {
      throw new Error('The current user response did not include data.');
    }

    return response.data;
  }
}
