import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { UserInfo } from '@/types/user'
import { login as loginApi } from '@/services/auth'
import { setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref<UserInfo | null>(null)
  const token = ref<string>('')

  const login = async (loginData: {
    username: string
    password: string
    remember: boolean
  }) => {
    try {
      const response = await loginApi(loginData)
      const { token: accessToken, user } = response.data
      token.value = accessToken
      userInfo.value = user
      setToken(accessToken, loginData.remember)
      return response
    } catch (error) {
      console.error('Login failed:', error)
      throw error
    }
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    removeToken()
  }

  const setUserInfo = (info: UserInfo) => {
    userInfo.value = info
  }

  return {
    userInfo,
    token,
    login,
    logout,
    setUserInfo
  }
}) 