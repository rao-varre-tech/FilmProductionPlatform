import {
    HttpClient,
    HttpHeaders,
    HttpParams
} from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class HttpService {

    private readonly http = inject(HttpClient);

    get<T>(
        url: string,
        params?: Record<string, string | number | boolean>,
        headers?: Record<string, string>
    ): Observable<T> {

        let httpParams = new HttpParams();

        if (params) {
            Object.entries(params).forEach(([key, value]) => {
                httpParams = httpParams.set(key, String(value));
            });
        }

        const httpHeaders = new HttpHeaders(headers);

        return this.http.get<T>(url, {
            params: httpParams,
            headers: httpHeaders
        });
    }

    post<TRequest, TResponse>(
        url: string,
        body: TRequest,
        headers?: Record<string, string>
    ): Observable<TResponse> {

        const httpHeaders = new HttpHeaders(headers);

        return this.http.post<TResponse>(url, body, {
            headers: httpHeaders
        });
    }

    put<TRequest, TResponse>(
        url: string,
        body: TRequest,
        headers?: Record<string, string>
    ): Observable<TResponse> {

        const httpHeaders = new HttpHeaders(headers);

        return this.http.put<TResponse>(url, body, {
            headers: httpHeaders
        });
    }

    patch<TRequest, TResponse>(
        url: string,
        body: TRequest,
        headers?: Record<string, string>
    ): Observable<TResponse> {

        const httpHeaders = new HttpHeaders(headers);

        return this.http.patch<TResponse>(url, body, {
            headers: httpHeaders
        });
    }

    delete<T>(
        url: string,
        headers?: Record<string, string>
    ): Observable<T> {

        const httpHeaders = new HttpHeaders(headers);

        return this.http.delete<T>(url, {
            headers: httpHeaders
        });
    }
}