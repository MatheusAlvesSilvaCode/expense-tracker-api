import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  nameUser: string;
  emailUser: string;
  password: string;
}

@Injectable({ providedIn: 'root' })
export class Auth {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080';
  private readonly tokenKey = 'jwt_token';

  private readonly usersUrl = 'http://localhost:8080/users';

  register(payload: RegisterRequest): Observable<string> {
    return this.http.post(`${this.usersUrl}/save-users`, payload, {
      responseType: 'text',
    });
  }

  private get storage(): Storage | null {
    return typeof window !== 'undefined' && window.localStorage ? window.localStorage : null;
  }

  login(credentials: LoginRequest): Observable<string> {
    return this.http.post(`${this.apiUrl}/auth/login`, credentials, { responseType: 'text' }).pipe(
      tap((token) => {
        this.storage?.setItem(this.tokenKey, token);
      }),
    );
  }

  getToken(): string | null {
    return this.storage?.getItem(this.tokenKey) ?? null;
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    this.storage?.removeItem(this.tokenKey);
  }
}
