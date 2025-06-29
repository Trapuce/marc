<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { useAuthStore } from '../stores/auth'
import { PrimeIcons } from '@primevue/core/api'
import { Button, useDialog } from 'primevue'
import type { Employee } from '../models'
import EmployeesList from '../components/EmployeesList.vue'
import EmployeeDetailsDrawer from '../components/EmployeeDetailsDrawer.vue'
import { ref } from 'vue'
import CreateEmployeeModal from '../components/CreateEmployeeModal.vue'
import AddEmployeesToProject from '../components/AddEmployeesToProject.vue'

const { t } = useI18n()
const dialog = useDialog()
const authStore = useAuthStore()

const createEmployeeClick = () => {
  dialog.open(CreateEmployeeModal, {
    props: {
      header: t('employees.new.header'),
      style: {
        width: '35vw'
      },
      breakpoints: {
        '960px': '75vw',
        '640px': '90vw'
      },
      modal: true,
      draggable: false
    }
  })
}

const addEmployeesToProject = (employees: Employee[]) => {
  dialog.open(AddEmployeesToProject, {
    props: {
      header: t('employees.add-to-project.header'),
      style: {
        width: 'auto',
        minWidth: '35vw',
        maxWidth: '90vw'
      },
      modal: true,
      draggable: false
    },
    data: {
      employees
    }
  })
  console.log('Adding employees to project', employees)
}
const isDrawerVisible = ref(false)
const selectedEmployee = ref<Employee | null>(null)
const openEmployeeDetails = (data: Employee) => {
  selectedEmployee.value = data
  isDrawerVisible.value = true
}
</script>

<template>
  <EmployeeDetailsDrawer
    v-if="selectedEmployee"
    :employee="selectedEmployee"
    v-model:visible="isDrawerVisible"
  />
  <div>
    <div class="flex justify-between">
      <h1 class="text-2xl font-bold">{{ $t('employees.header') }}</h1>
      <Button
        v-if="authStore.role === 'hr'"
        :icon="PrimeIcons.PLUS"
        :label="t('employees.create')"
        size="small"
        @click="createEmployeeClick"
      />
    </div>
    <div class="mt-4">
      <EmployeesList
        @openDetails="openEmployeeDetails"
        @addEmployeesToProject="addEmployeesToProject"
        editMode
      />
    </div>
  </div>
</template>
