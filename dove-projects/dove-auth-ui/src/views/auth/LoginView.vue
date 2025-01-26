<template>
  <div class="login-container">
    <div class="login-box">
      <!-- Logo -->
      <div class="logo">
        <img src="@/assets/logo.svg" alt="Atlassian" width="180" />
      </div>

      <!-- Title -->
      <h1 class="title">{{ $t('auth.login.title') }}</h1>

      <!-- Login Form -->
      <a-form
        :model="formState"
        @finish="handleSubmit"
        class="login-form"
      >
        <!-- Email Field -->
        <a-form-item name="email">
          <a-input
            v-model:value="formState.email"
            :placeholder="$t('auth.login.email.placeholder')"
            size="large"
            class="email-input"
          />
        </a-form-item>

        <!-- Remember Me -->
        <a-form-item name="rememberMe" class="remember-me">
          <a-checkbox v-model:checked="formState.rememberMe">
            {{ $t('auth.login.remember_me') }}
            <a-tooltip placement="right">
              <template #title>
                {{ $t('auth.login.remember_me_tip') }}
              </template>
              <info-circle-outlined class="help-icon" />
            </a-tooltip>
          </a-checkbox>
        </a-form-item>

        <!-- Continue Button -->
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            size="large"
            :loading="loading"
            block
            class="continue-button"
          >
            {{ $t('common.continue') }}
          </a-button>
        </a-form-item>

        <!-- Social Login Divider -->
        <div class="divider">
          <span>{{ $t('auth.login.or') }}</span>
        </div>

        <!-- Social Login Buttons -->
        <div class="social-login">
          <a-button
            v-for="provider in socialProviders"
            :key="provider.name"
            class="social-button"
            block
            @click="handleSocialLogin(provider.name)"
          >
            <template #icon>
              <img :src="provider.icon" :alt="provider.name" class="social-icon" />
            </template>
            {{ provider.name }}
          </a-button>
        </div>

        <!-- Footer Links -->
        <div class="footer-links">
          <a @click="handleForgotPassword">{{ $t('auth.login.cant_login') }}</a>
          <a-divider type="vertical" />
          <a @click="handleCreateAccount">{{ $t('auth.login.create_account') }}</a>
        </div>
      </a-form>

      <!-- Product Info -->
      <div class="product-info">
        <img src="@/assets/logo-small.svg" alt="Atlassian" width="24" class="logo-small" />
        <p>{{ $t('app.product_info') }}</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { InfoCircleOutlined } from '@ant-design/icons-vue'
import type { LoginRequest } from '@/types/auth'

// Router
const router = useRouter()

// Auth store
const authStore = useAuthStore()

// Loading state
const loading = ref(false)

// Form state
const formState = reactive<LoginRequest & { rememberMe: boolean }>({
  email: '',
  password: '',
  rememberMe: false
})

// Social providers
const socialProviders = [
  {
    name: 'Google',
    icon: new URL('@/assets/icons/google.svg', import.meta.url).href
  },
  {
    name: 'Microsoft',
    icon: new URL('@/assets/icons/microsoft.svg', import.meta.url).href
  },
  {
    name: 'Apple',
    icon: new URL('@/assets/icons/apple.svg', import.meta.url).href
  },
  {
    name: 'Slack',
    icon: new URL('@/assets/icons/slack.svg', import.meta.url).href
  }
]

// Handle form submit
const handleSubmit = async () => {
  try {
    loading.value = true
    await authStore.handleLogin(formState)
  } finally {
    loading.value = false
  }
}

// Handle social login
const handleSocialLogin = async (provider: string) => {
  try {
    loading.value = true
    const { url } = await authStore.getSocialLoginUrl(provider)
    window.location.href = url
  } finally {
    loading.value = false
  }
}

// Handle forgot password
const handleForgotPassword = () => {
  router.push('/forgot-password')
}

// Handle create account
const handleCreateAccount = () => {
  router.push('/register')
}
</script>

<style lang="less" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #FFFFFF;
  background-image: linear-gradient(to bottom right, #DEEBFF 0%, #FFFFFF 100%);
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 32px 40px;
  background: #FFFFFF;
  border-radius: 3px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 10px;
}

.logo {
  text-align: center;
  margin-bottom: 32px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #172B4D;
  text-align: center;
  margin-bottom: 24px;
}

.login-form {
  :deep(.ant-form-item) {
    margin-bottom: 16px;
  }
}

.email-input {
  height: 44px;
  border-radius: 3px;
  border: 2px solid #DFE1E6;
  
  &:hover, &:focus {
    border-color: #4C9AFF;
  }
}

.remember-me {
  margin-top: -4px;
  
  :deep(.ant-checkbox-wrapper) {
    color: #42526E;
    font-size: 14px;
  }
  
  .help-icon {
    color: #42526E;
    margin-left: 4px;
    font-size: 14px;
  }
}

.continue-button {
  height: 44px;
  font-size: 14px;
  font-weight: 500;
  background: #0052CC;
  border: none;
  border-radius: 3px;
  
  &:hover {
    background: #0065FF;
  }
}

.divider {
  display: flex;
  align-items: center;
  margin: 24px 0;
  
  &::before,
  &::after {
    content: '';
    flex: 1;
    border-top: 1px solid #DFE1E6;
  }
  
  span {
    padding: 0 16px;
    color: #42526E;
    font-size: 12px;
  }
}

.social-login {
  .social-button {
    height: 44px;
    margin-bottom: 8px;
    color: #42526E;
    font-size: 14px;
    font-weight: 500;
    border: 2px solid #DFE1E6;
    border-radius: 3px;
    padding: 0 12px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    
    &:hover {
      background: #F4F5F7;
      border-color: #DFE1E6;
    }
    
    .social-icon {
      width: 24px;
      height: 24px;
      margin-right: 12px;
    }
    
    :deep(.ant-btn-icon) {
      margin-right: 0;
    }
  }
}

.footer-links {
  margin-top: 24px;
  text-align: center;
  font-size: 14px;
  
  a {
    color: #0052CC;
    text-decoration: none;
    cursor: pointer;
    
    &:hover {
      color: #0065FF;
      text-decoration: underline;
    }
  }
  
  :deep(.ant-divider) {
    margin: 0 8px;
    border-color: #DFE1E6;
  }
}

.product-info {
  margin-top: 48px;
  text-align: center;
  
  .logo-small {
    margin-bottom: 8px;
  }
  
  p {
    color: #42526E;
    font-size: 12px;
    margin: 0;
  }
}
</style> 