import { createI18n } from 'vue-i18n'
import type { I18n } from 'vue-i18n'

// Import language files
import enUS from './en-US'
import zhCN from './zh-CN'

// Create i18n instance
const i18n: I18n = createI18n({
  legacy: false, // you must set `false`, to use Composition API
  locale: import.meta.env.VITE_APP_I18N_LOCALE || 'zh-CN',
  fallbackLocale: import.meta.env.VITE_APP_I18N_FALLBACK_LOCALE || 'en-US',
  messages: {
    'en-US': enUS,
    'zh-CN': zhCN
  },
  globalInjection: true,
  silentTranslationWarn: true,
  silentFallbackWarn: true
})

export default i18n 