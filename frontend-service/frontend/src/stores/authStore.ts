import { persistentMap } from '@nanostores/persistent'
import type { TokenResponse } from '../types/TokenResponse'

export const authStore = persistentMap<TokenResponse>('authToken:', {
    accessToken: "",
    expiresIn: 0,
    refreshExpiresIn: 0,
    refreshToken: "",
    authenticated: false
}, {
  encode: JSON.stringify,
  decode: JSON.parse,
});