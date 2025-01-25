<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { message } from 'ant-design-vue'
import type { FormInstance } from 'ant-design-vue'
import { useUserStore } from '@/stores/user'

interface FormState {
  username: string
  password: string
  remember: boolean
}

const formRef = ref<FormInstance>()
const loading = ref(false)
const { t } = useI18n()
const router = useRouter()
const userStore = useUserStore()

const formState = reactive<FormState>({
  username: '',
  password: '',
  remember: true
})

const rules = {
  username: [
    { required: true, message: t('login.error.username'), trigger: 'blur' }
  ],
  password: [
    { required: true, message: t('login.error.password'), trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  try {
    loading.value = true
    await formRef.value?.validate()
    await userStore.login({
      username: formState.username,
      password: formState.password,
      remember: formState.remember
    })
    message.success(t('login.success'))
    router.push('/')
  } catch (error) {
    console.error('Login error:', error)
    message.error(t('login.error.invalid'))
  } finally {
    loading.value = false
  }
}

const handleRegister = () => {
  router.push('/register')
}

const handleForgotPassword = () => {
  router.push('/forgot-password')
}
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="login-title">{{ t('login.title') }}</h1>
      <a-form
        :model="formState"
        :rules="rules"
        ref="formRef"
        @finish="handleSubmit"
      >
        <a-form-item name="username">
          <a-input
            v-model:value="formState.username"
            :placeholder="t('login.username')"
            size="large"
          >
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item name="password">
          <a-input-password
            v-model:value="formState.password"
            :placeholder="t('login.password')"
            size="large"
          >
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <div class="login-options">
            <a-checkbox v-model:checked="formState.remember">
              {{ t('login.remember') }}
            </a-checkbox>
            <a class="forgot-link" @click="handleForgotPassword">
              {{ t('login.forgot') }}
            </a>
          </div>
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            :loading="loading"
            block
            size="large"
          >
            {{ t('login.submit') }}
          </a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="link" block @click="handleRegister">
            {{ t('login.register') }}
          </a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 32px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.login-title {
  text-align: center;
  margin-bottom: 32px;
  font-size: 24px;
  color: rgba(0, 0, 0, 0.85);
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-link {
  color: #1890ff;
  cursor: pointer;
}

.forgot-link:hover {
  color: #40a9ff;
}
</style> 