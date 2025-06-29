<script setup lang="ts">
import {FloatLabel, InputText } from 'primevue'
import { ref, watch } from 'vue';


const props = defineProps<{
  label: string
  modelValue: string
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const localValue = ref(props.modelValue); // Local state

watch(() => props.modelValue, (newVal) => {
  localValue.value = newVal;
});

const updateValue = (event: Event) => {
  const target = event.target as HTMLInputElement;
  emit('update:modelValue', target.value);
};
</script>

<template>
 <FloatLabel variant="on">
      <InputText variant="outlined" id="label" :value="modelValue" @input="updateValue" />
      <label for="label">{{ label }}</label>
    </FloatLabel>
</template>
