<script setup lang="ts">
import { Button } from 'primevue'
import { PrimeIcons } from '@primevue/core/api'
import { Form, type FormSubmitEvent } from '@primevue/forms'
import EmployeesList from './EmployeesList.vue'
import type { Employee } from '../models'
import { ref } from 'vue'
import { useProjectStore } from '../stores/projects'

const props = defineProps<{
  forcedDates: Date[]
}>()

const projectsStore = useProjectStore()
const emit = defineEmits(['back', 'submit'])

const selectedEmployees = ref<Employee[]>([])
const updateSelectedEmployees = ({ employees }: { employees: Employee[] }) => {
  selectedEmployees.value = employees
}

const onFormSubmit = ({ valid }: FormSubmitEvent) => {
  if (valid) {
    emit('submit', { employees : selectedEmployees.value} )
  }
}
</script>

<template>
  <Form @submit="onFormSubmit" class="flex flex-col gap-4 p-2 w-full">
    <EmployeesList @updateSelectedEmployees="updateSelectedEmployees" :forcedDates="props.forcedDates" />
    <div class="flex pt-6 justify-between">
      <Button
        severity="secondary"
        label="Back"
        :icon="PrimeIcons.ARROW_LEFT"
        iconPos="left"
        @click="emit('back')"
      />
      <Button label="Create" type="submit" :loading="projectsStore.loading" />
    </div>
  </Form>
</template>
