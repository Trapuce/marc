<script setup lang="ts">
import { useAuthStore } from '../stores/auth'
import { useI18n } from 'vue-i18n'
import { Button } from 'primevue'
import Logo from '../components/Logo.vue'

const { t } = useI18n()


const authStore = useAuthStore()

const handleLogin = async () => {
  authStore.login()
}
</script>

<template>
  <div class="flex h-screen">
    <div
      class="bg-primary relative w-1/2 flex-col items-center overflow-hidden justify-center hidden md:flex"
    >
      <div class="absolute top-0 left-0 w-screen px-2">
        <img
          class="w-full"
          src="/marc_white_outlined.svg"
          alt="Marc logo"
          style="transform: translateY(-70%)"
        />
      </div>
      <div class="absolute bottom-0 left-0 w-screen px-2">
        <img
          class="w-full"
          src="/marc_white_outlined.svg"
          alt="Marc logo"
          style="transform: translateY(40%)"
        />
      </div>
      <Logo :colored="false" :height="200" :width="200" />
    </div>

    <div class="relative w-full overflow-hidden md:w-1/2 flex flex-col items-center justify-center">
      <div class="absolute top-0 right-0 w-screen px-2">
        <img
          class="w-full"
          src="/marc_red_outlined.svg"
          alt="Marc logo"
          style="transform: translateY(-70%)"
        />
      </div>
      <div class="absolute bottom-0 right-0 w-screen px-2">
        <img
          class="w-full"
          src="/marc_red_outlined.svg"
          alt="Marc logo"
          style="transform: translateY(40%)"
        />
      </div>
      <Logo :colored="true" :height="200" :width="200" class="m-6 md:hidden" />

      <h2 class="font-secondary text-4xl m-6">{{ t('login.welcome') }}</h2>
      <Button class="my-2" @click="handleLogin">
          {{ t('login.connexion') }}
        </Button>
    </div>
  </div>
</template>
