<script setup lang="ts">
import { ref } from 'vue'

const iconClass = ref('pi-moon')

const onThemeToggler = () => {
  const root = document.getElementsByTagName('html')[0]
  root.classList.toggle('p-dark')
  iconClass.value = iconClass.value === 'pi-moon' ? 'pi-sun' : 'pi-moon'
}
</script>

<template>
  <button
    type="button"
    class="inline-flex w-8 h-8 p-0 items-center justify-center surface-0 dark:surface-800 cursor-pointer"
    @click="onThemeToggler"
  >
    <i :class="`pi ${iconClass}`" />
  </button>
</template>
