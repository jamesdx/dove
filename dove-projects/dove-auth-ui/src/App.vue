<script setup lang="ts">
import { RouterView } from 'vue-router'
import { ConfigProvider } from 'ant-design-vue'
import { useI18n } from 'vue-i18n'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import enUS from 'ant-design-vue/es/locale/en_US'
import { computed } from 'vue'

const { locale } = useI18n()

const antLocale = computed(() => {
  return locale.value === 'zh-CN' ? zhCN : enUS
})
</script>

<template>
  <ConfigProvider :locale="antLocale">
    <RouterView />
  </ConfigProvider>
</template>

<style>
#app {
  width: 100%;
  height: 100vh;
  margin: 0;
  padding: 0;
}
</style> 