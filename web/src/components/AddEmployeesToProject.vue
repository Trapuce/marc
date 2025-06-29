<script setup lang="ts">
import { inject, onMounted, ref } from 'vue'
import { Button, Fieldset, Tag } from 'primevue'
import { Form } from '@primevue/forms'
import type { Employee, Project } from '../models'
import ProjectsList from './ProjectsList.vue'
import { useI18n } from 'vue-i18n'
import { useToast } from 'primevue/usetoast'
import { useProjectStore } from '../stores/projects'

const { t } = useI18n()
const toast = useToast()
const projectsStore = useProjectStore()

const dialogRef = inject<{ value: { data: { employees: Employee[] }; close: () => void } }>(
  'dialogRef'
)
const closeDialog = () => {
  dialogRef?.value.close()
}
const employees = ref<Employee[]>([])
const selectedProject = ref()

const projectSelected = (event: { data: Project }) => {
  selectedProject.value = event.data
}

const onFormSubmit = () => {

  try {
    projectsStore.addEmployees(selectedProject.value.id, employees.value.map((e) => e.id))
  } catch (error) {
    toast.add({
      severity: 'error',
      summary: 'Error adding employees to project',
      life: 3000
    })
    return
  }

  toast.add({
    severity: 'info',
    summary: 'Employees successfully added to ' + selectedProject.value.name,
    detail: 'Message Content',
    life: 3000
  })
  closeDialog()
}

onMounted(() => {
  if (dialogRef?.value?.data?.employees) {
    employees.value = dialogRef.value.data.employees
  }
})
</script>

<template>
  <Form @submit="onFormSubmit">
    <Fieldset :legend="$t('employees.add-to-project.selected')">
      <Tag
        v-for="employee in employees"
        :key="employee.id"
        class="mr-2 mb-2"
        severity="secondary"
        :value="employee.firstname + ' ' + employee.lastname"
      />
    </Fieldset>
    <p class="text-gray-500 text-md mt-4">{{ t('employees.add-to-project.description') }}</p>
    <ProjectsList mode="select" @projectSelected="projectSelected" />
    <div class="flex pt-6 justify-between">
      <Button
        severity="secondary"
        :label="t('employees.add-to-project.cancel')"
        @click="closeDialog"
      />
      <Button :label="t('employees.add-to-project.add')" type="submit" />
    </div>
  </Form>
</template>
