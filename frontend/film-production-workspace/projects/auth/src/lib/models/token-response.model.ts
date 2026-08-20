export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: 'Bearer';
  // The backend currently returns milliseconds, despite its documentation saying seconds.
  expiresIn: number;
}

export type LoginResponse = TokenResponse;
export type RefreshTokenResponse = TokenResponse;
