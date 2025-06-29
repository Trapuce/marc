<script setup lang="ts">
import { ref, computed } from 'vue'
import { Menu, Slider, InputText, IconField, InputIcon } from 'primevue'

const props = defineProps({
  modelValue: {
    type: Number,
    default: 0,
    validator: (value: number) => value >= 0 && value <= 30
  }
})

const emit = defineEmits(['update:modelValue'])

const model = computed<number>({
  get: () => props.modelValue,
  set: (value: number) => emit("update:modelValue", value),
});

const overlayPanel = ref()

const toggleOverlay = (event: Event) => {
  overlayPanel.value.toggle(event)
}

const buttonLabel = computed(() => {
  return `${model.value} ans d'expérience`
})
</script>

<template>
  <IconField class="cursor-pointer">
    <InputText
      class="text-grey min-w-72 cursor-pointer"
      :placeholder="buttonLabel"
      readonly
      @click="toggleOverlay"
      size="small"
    />
    <InputIcon>
      <i class="pi pi-chevron-down" />
    </InputIcon>
  </IconField>
  <Menu ref="overlayPanel" :popup="true">
    <template #start>
      <div class="flex items-center gap-4 min-w-64 p-4">
        <label for="experienceRange">{{ $t('models.employee.experience')}}</label>
        <Slider class="min-w-32" v-model="model" :min="0" :max="30" />
        <p>{{ model }} {{ $t('models.employee.years')}}</p>
      </div>
    </template>
  </Menu>
</template>