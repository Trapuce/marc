<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
width : number
height : number
colored? : boolean 
}>()

const pathLogo = computed(() => {
  return `/marc_${props.colored ? "red" : "white"}.svg`;
});
</script>



<template>

  <img :src="pathLogo" class="logo" alt="Marc logo" :width="props.width" :height="props.height" />
</template>
