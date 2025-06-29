<script setup lang="ts">
import { useAuthStore } from '../stores/auth';

const authStore = useAuthStore()
</script>

<template>
  <div class="w-full h-screen flex flex-col items-center justify-center">
    <h1 class="text-primary text-3xl font-black">Votre role n'est pas enregistré </h1>
    <p class="text-lg text-center text-gray-600">Rapprochez vous de votre administrateur</p>
    <Button @click="authStore.logout" class="text-primary hover:underline">Deconnexion</Button>
  </div>
</template>
