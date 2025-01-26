export interface User {
  id: number
  username: string
  email: string
  firstName?: string
  lastName?: string
  status: number
  accountType: number
  lastLoginTime?: string
  lastLoginIp?: string
  locale: string
  timezone: string
  dateFormat: string
  timeFormat: string
  currency: string
  region?: string
  createdAt: string
  updatedAt: string
}

export interface LoginRequest {
  email: string
  password: string
  rememberMe?: boolean
  deviceInfo?: DeviceInfo
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  confirmPassword: string
  accountType?: number
  locale?: string
  timezone?: string
  region?: string
}

export interface LoginResponse {
  access_token: string
  refresh_token: string
  expires_in: number
  token_type: string
  user: User
}

export interface RefreshTokenResponse {
  access_token: string
  refresh_token: string
  expires_in: number
  token_type: string
}

export interface DeviceInfo {
  deviceId?: string
  deviceType?: string
  deviceName?: string
  deviceOs?: string
  deviceBrowser?: string
  deviceIp?: string
}

export interface MfaRequest {
  type: string
  code: string
  deviceInfo?: DeviceInfo
}

export interface SocialLoginRequest {
  provider: string
  code: string
  state: string
  deviceInfo?: DeviceInfo
}

export interface PasswordResetRequest {
  token: string
  password: string
  confirmPassword: string
}

export interface EmailVerificationRequest {
  token: string
} 