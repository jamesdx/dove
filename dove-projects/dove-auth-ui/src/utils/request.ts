import axios, { AxiosError, type AxiosRequestConfig } from 'axios'
import { useAuthStore } from '@/stores/auth'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL,
  timeout: 10000
})

// Request interceptor
service.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    const { locale } = useI18n()

    // Add auth token
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`
    }

    // Add locale
    config.headers['Accept-Language'] = locale.value

    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  (response) => {
    return response.data
  },
  async (error: AxiosError) => {
    const authStore = useAuthStore()
    const { t } = useI18n()

    if (error.response) {
      const { status } = error.response

      switch (status) {
        case 401:
          // Token expired
          if (authStore.refreshToken) {
            try {
              await authStore.handleRefreshToken()
              // Retry original request
              const config = error.config as AxiosRequestConfig
              return service(config)
            } catch (refreshError) {
              message.error(t('error.session_expired'))
              await authStore.handleLogout()
            }
          } else {
            message.error(t('error.unauthorized'))
            await authStore.handleLogout()
          }
          break

        case 403:
          message.error(t('error.forbidden'))
          break

        case 404:
          message.error(t('error.not_found'))
          break

        case 429:
          message.error(t('error.too_many_requests'))
          break

        case 500:
          message.error(t('error.server_error'))
          break

        default:
          message.error(t('error.unknown'))
      }
    } else if (error.request) {
      message.error(t('error.network'))
    } else {
      message.error(error.message || t('error.unknown'))
    }

    return Promise.reject(error)
  }
)

export default service 