import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { message } from 'ant-design-vue'
import { getToken } from './auth'
import router from '@/router'

// Create axios instance
const service: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_APP_API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor
service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// Response interceptor
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data

    // If the custom code is not 200, it is judged as an error.
    if (res.code !== 200) {
      message.error(res.message || 'Error')

      // 401: Token expired;
      if (res.code === 401) {
        router.push('/login')
      }
      return Promise.reject(new Error(res.message || 'Error'))
    }
    return res
  },
  (error) => {
    console.error('Response error:', error)
    message.error(error.message || 'Request Error')
    return Promise.reject(error)
  }
)

const request = async <T = any>(config: AxiosRequestConfig): Promise<T> => {
  try {
    const response = await service(config)
    return response as T
  } catch (error) {
    return Promise.reject(error)
  }
}

export default request 