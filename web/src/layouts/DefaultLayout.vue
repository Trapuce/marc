<script setup lang="ts">
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import HorizontalContainer from '../components/HorizontalContainer.vue'
</script>

<template>
  <div class="min-h-screen max-h-screen flex flex-col">
    <Header />
    <main class="flex-1 py-4">
      <HorizontalContainer>
        <RouterView />
      </HorizontalContainer>
    </main>
    <Footer />
  </div>
</template>
