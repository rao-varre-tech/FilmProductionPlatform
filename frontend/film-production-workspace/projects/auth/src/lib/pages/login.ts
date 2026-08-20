import { ChangeDetectionStrategy, Component, Signal, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { AuthUser, LoginRequest } from '../models';
import { AuthService } from '../services';
import { AuthState } from '../state';

@Component({
  selector: 'fp-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent {
  private readonly authService = inject(AuthService);
  private readonly authState = inject(AuthState);

  readonly user: Signal<AuthUser | null> = this.authState.user;
  readonly isSubmitting = signal(false);
  readonly errorMessage = signal<string | null>(null);

  readonly loginForm = new FormGroup({
    username: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
    password: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required],
    }),
  });

  submit(): void {
    if (this.isSubmitting()) {
      return;
    }

    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const request: LoginRequest = this.loginForm.getRawValue();

    this.errorMessage.set(null);
    this.isSubmitting.set(true);

    this.authService.login(request).subscribe({
      next: () => {
        this.isSubmitting.set(false);
      },
      error: (error: unknown) => {
        this.isSubmitting.set(false);
        this.errorMessage.set(this.getErrorMessage(error));
      },
    });
  }

  private getErrorMessage(error: unknown): string {
    if (!(error instanceof HttpErrorResponse)) {
      return 'Unable to sign in. Please try again.';
    }

    switch (error.status) {
      case 0:
        return 'Unable to reach the service. Please check your connection and try again.';
      case 401:
        return 'The username or password is incorrect.';
      case 403:
        return 'Please verify your email before signing in.';
      case 423:
        return 'This account is temporarily locked. Please try again later.';
      default:
        return 'Unable to sign in. Please try again.';
    }
  }
}
