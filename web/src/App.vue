<script setup lang="ts">
import { DynamicDialog } from 'primevue';
import Toast from 'primevue/toast';
</script>

<template>
  <RouterView />
  <DynamicDialog />
  <Toast />
</template>
