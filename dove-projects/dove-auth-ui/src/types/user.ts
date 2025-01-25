export interface UserInfo {
  id: number
  username: string
  email: string
  avatar?: string
  roles: string[]
  permissions: string[]
  tenantId: number
  locale: string
  timezone: string
  lastLoginTime: string
  status: number
  createdAt: string
  updatedAt: string
}

export interface LoginRequest {
  username: string
  password: string
  remember: boolean
}

export interface LoginResponse {
  token: string
  user: UserInfo
}

export interface RegisterRequest {
  username: string
  email: string
  password: string
  confirmPassword: string
}

export interface RegisterResponse {
  message: string
  user: UserInfo
}

export interface ForgotPasswordRequest {
  email: string
}

export interface ForgotPasswordResponse {
  message: string
}

export interface ResetPasswordRequest {
  token: string
  password: string
  confirmPassword: string
}

export interface ResetPasswordResponse {
  message: string
} 