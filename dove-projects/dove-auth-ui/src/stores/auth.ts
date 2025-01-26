import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import type { LoginRequest, RegisterRequest, User } from '@/types/auth'
import { login, register, logout, refreshToken } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const { t } = useI18n()
  const router = useRouter()

  // State
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const refreshTokenValue = ref<string | null>(null)
  const loading = ref(false)

  // Getters
  const isAuthenticated = computed(() => !!token.value)
  const userFullName = computed(() => user.value ? `${user.value.firstName} ${user.value.lastName}` : '')

  // Actions
  async function handleLogin(loginData: LoginRequest) {
    try {
      loading.value = true
      const response = await login(loginData)
      
      token.value = response.access_token
      refreshTokenValue.value = response.refresh_token
      user.value = response.user

      // Store tokens
      localStorage.setItem(import.meta.env.VITE_AUTH_TOKEN_KEY, response.access_token)
      localStorage.setItem(import.meta.env.VITE_AUTH_REFRESH_TOKEN_KEY, response.refresh_token)

      message.success(t('auth.login.success'))
      
      // Redirect
      const redirect = router.currentRoute.value.query.redirect as string
      await router.push(redirect || '/dashboard')
    } catch (error: any) {
      message.error(error.message || t('auth.login.error'))
      throw error
    } finally {
      loading.value = false
    }
  }

  async function handleRegister(registerData: RegisterRequest) {
    try {
      loading.value = true
      await register(registerData)
      message.success(t('auth.register.success'))
      await router.push('/login')
    } catch (error: any) {
      message.error(error.message || t('auth.register.error'))
      throw error
    } finally {
      loading.value = false
    }
  }

  async function handleLogout() {
    try {
      loading.value = true
      await logout()
      
      // Clear state
      user.value = null
      token.value = null
      refreshTokenValue.value = null
      
      // Clear storage
      localStorage.removeItem(import.meta.env.VITE_AUTH_TOKEN_KEY)
      localStorage.removeItem(import.meta.env.VITE_AUTH_REFRESH_TOKEN_KEY)

      message.success(t('auth.logout.success'))
      await router.push('/login')
    } catch (error: any) {
      message.error(error.message || t('auth.logout.error'))
    } finally {
      loading.value = false
    }
  }

  async function handleRefreshToken() {
    try {
      if (!refreshTokenValue.value) {
        throw new Error('No refresh token available')
      }

      const response = await refreshToken(refreshTokenValue.value)
      token.value = response.access_token
      refreshTokenValue.value = response.refresh_token

      // Update storage
      localStorage.setItem(import.meta.env.VITE_AUTH_TOKEN_KEY, response.access_token)
      localStorage.setItem(import.meta.env.VITE_AUTH_REFRESH_TOKEN_KEY, response.refresh_token)
    } catch (error) {
      // If refresh fails, logout user
      await handleLogout()
      throw error
    }
  }

  // Initialize state from storage
  function init() {
    const storedToken = localStorage.getItem(import.meta.env.VITE_AUTH_TOKEN_KEY)
    const storedRefreshToken = localStorage.getItem(import.meta.env.VITE_AUTH_REFRESH_TOKEN_KEY)

    if (storedToken && storedRefreshToken) {
      token.value = storedToken
      refreshTokenValue.value = storedRefreshToken
    }
  }

  return {
    // State
    user,
    token,
    refreshToken: refreshTokenValue,
    loading,

    // Getters
    isAuthenticated,
    userFullName,

    // Actions
    handleLogin,
    handleRegister,
    handleLogout,
    handleRefreshToken,
    init
  }
}) 