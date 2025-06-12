export interface TokenResponse {
    accessToken: string;
    expiresIn: number;
    refreshExpiresIn: number | null;
    refreshToken: string;
    authenticated: boolean
}
