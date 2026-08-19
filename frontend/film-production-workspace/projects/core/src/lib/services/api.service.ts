import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { API_CONFIG } from '../config/api';
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private readonly apiConfig = inject(API_CONFIG);
  private readonly http = inject(HttpService);

  private buildUrl(endpoint: string): string {
    const gateway = this.apiConfig.gateway.replace(/\/$/, '');
    const path = endpoint.startsWith('/') ? endpoint : `/${endpoint}`;

    return `${gateway}${path}`;
  }

  get<T>(endpoint: string): Observable<T> {
    return this.http.get<T>(this.buildUrl(endpoint));
  }

  post<TRequest, TResponse>(
    endpoint: string,
    body: TRequest
  ): Observable<TResponse> {
    return this.http.post<TRequest, TResponse>(
      this.buildUrl(endpoint),
      body
    );
  }

  put<TRequest, TResponse>(
    endpoint: string,
    body: TRequest
  ): Observable<TResponse> {
    return this.http.put<TRequest, TResponse>(
      this.buildUrl(endpoint),
      body
    );
  }

  patch<TRequest, TResponse>(
    endpoint: string,
    body: TRequest
  ): Observable<TResponse> {
    return this.http.patch<TRequest, TResponse>(
      this.buildUrl(endpoint),
      body
    );
  }

  delete<T>(endpoint: string): Observable<T> {
    return this.http.delete<T>(this.buildUrl(endpoint));
  }
}