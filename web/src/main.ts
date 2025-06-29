import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import i18n from './i18n'
import router from './router'
import PrimeVue from 'primevue/config'

import DialogService from 'primevue/dialogservice'
import ToastService from 'primevue/toastservice'
import ConfirmationService from 'primevue/confirmationservice'

import './style.css'
import { useAuthStore } from './stores/auth'
import CustomPreset from './theme/preset'

import Tooltip from 'primevue/tooltip'

const pinia = createPinia()

const renderApp = async () => {
  const app = createApp(App)
  app.use(pinia)
  app.use(i18n)
  app.use(router)
  app.use(ToastService)
  app.use(ConfirmationService);
  app.use(PrimeVue, {
    theme: {
      preset: CustomPreset,
      options: {
        prefix: 'p',
        darkModeSelector: '.p-dark',
        cssLayer: false
      }
    },
    dynamicDialog: true
  })
  app.use(DialogService)
  app.directive('tooltip', Tooltip)

  const authStore = useAuthStore()
  await authStore.initializeAuth()

  app.mount('#app')
}

;(async () => {
  await renderApp()
})()
