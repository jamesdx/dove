import request from '@/utils/request'
import type {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  RegisterResponse,
  ForgotPasswordRequest,
  ForgotPasswordResponse,
  ResetPasswordRequest,
  ResetPasswordResponse
} from '@/types/user'

export function login(data: LoginRequest) {
  return request<LoginResponse>({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function register(data: RegisterRequest) {
  return request<RegisterResponse>({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function forgotPassword(data: ForgotPasswordRequest) {
  return request<ForgotPasswordResponse>({
    url: '/auth/forgot-password',
    method: 'post',
    data
  })
}

export function resetPassword(data: ResetPasswordRequest) {
  return request<ResetPasswordResponse>({
    url: '/auth/reset-password',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

export function getUserInfo() {
  return request<LoginResponse>({
    url: '/auth/user-info',
    method: 'get'
  })
} 