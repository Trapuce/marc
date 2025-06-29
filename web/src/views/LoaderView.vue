<script setup lang="ts">
import { useRouter } from 'vue-router'
import KeyCloakService from '../services/keycloak'
import ProgressSpinner from 'primevue/progressspinner'
import { useI18n } from 'vue-i18n'
import { useAuthStore } from '../stores/auth'

const { t } = useI18n()
const router = useRouter()

function delay(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms))
}

// Wait for Keycloak to be ready and determine the role
async function checkUserRole() {
  if (!KeyCloakService.IsLoggedIn) {
    await KeyCloakService.Init()
  }
  //await delay(1000)

  const authStore = useAuthStore()
  console.log ("ceci est mon role ")
  if(authStore.role=='hr'){
    router.replace('/employees')
  } else if(authStore.role=='manager'){
    router.replace('/projects')
  } else {
    router.replace('/norole')
  }
  //router.replace('/')
}

checkUserRole()
</script>

<template>
  <div class="h-screen flex flex-col items-center justify-center">
    <ProgressSpinner stroke-width="5"></ProgressSpinner>
    <h1>{{ t('login.loading') }}</h1>
  </div>
</template>
