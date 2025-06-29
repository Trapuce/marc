<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <div class="w-full h-screen flex flex-col items-center justify-center">
    <h1 class="text-primary text-8xl font-black">404</h1>
    <p class="text-lg text-center text-gray-600">La page que vous cherchez n'existe pas.</p>
    <RouterLink to="/" class="text-primary hover:underline">Retour à l'accueil</RouterLink>
  </div>
</template>
