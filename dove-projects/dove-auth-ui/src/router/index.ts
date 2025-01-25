import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login/index.vue'),
    meta: {
      title: 'route.login',
      hidden: true
    }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/register/index.vue'),
    meta: {
      title: 'route.register',
      hidden: true
    }
  },
  {
    path: '/forgot-password',
    name: 'forgot-password',
    component: () => import('../views/forgot-password/index.vue'),
    meta: {
      title: 'route.forgotPassword',
      hidden: true
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('../views/error/404.vue'),
    meta: {
      title: 'route.notFound',
      hidden: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  // Set page title
  document.title = `${import.meta.env.VITE_APP_TITLE} - ${to.meta.title}`
  next()
})

export default router 