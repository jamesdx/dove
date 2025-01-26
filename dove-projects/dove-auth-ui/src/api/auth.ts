import request from '@/utils/request'
import type { LoginRequest, RegisterRequest, LoginResponse, RefreshTokenResponse } from '@/types/auth'

export function login(data: LoginRequest): Promise<LoginResponse> {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function register(data: RegisterRequest): Promise<void> {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function logout(): Promise<void> {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

export function refreshToken(refreshToken: string): Promise<RefreshTokenResponse> {
  return request({
    url: '/auth/token/refresh',
    method: 'post',
    data: { refresh_token: refreshToken }
  })
}

export function verifyEmail(token: string): Promise<void> {
  return request({
    url: '/auth/verify-email',
    method: 'post',
    data: { token }
  })
}

export function forgotPassword(email: string): Promise<void> {
  return request({
    url: '/auth/forgot-password',
    method: 'post',
    data: { email }
  })
}

export function resetPassword(token: string, password: string): Promise<void> {
  return request({
    url: '/auth/reset-password',
    method: 'post',
    data: { token, password }
  })
}

export function sendMfaCode(type: string): Promise<void> {
  return request({
    url: '/auth/mfa/send-code',
    method: 'post',
    data: { type }
  })
}

export function verifyMfaCode(code: string): Promise<LoginResponse> {
  return request({
    url: '/auth/mfa/verify',
    method: 'post',
    data: { code }
  })
}

export function getSocialLoginUrl(provider: string): Promise<{ url: string }> {
  return request({
    url: `/auth/social/${provider}`,
    method: 'get'
  })
}

export function handleSocialCallback(provider: string, code: string, state: string): Promise<LoginResponse> {
  return request({
    url: `/auth/social/${provider}/callback`,
    method: 'get',
    params: { code, state }
  })
} 