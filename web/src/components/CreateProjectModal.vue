<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { Stepper, StepList, Step, StepPanel } from 'primevue'
import CreateProjectMainInfo from './CreateProjectMainInfo.vue'
import CreateProjectMembers from './CreateProjectMembers.vue'
import { ref, inject } from 'vue'
import { useToast } from 'primevue/usetoast'
import { useProjectStore } from '../stores/projects'

const projectsStore = useProjectStore()
const toast = useToast()
const { t } = useI18n()
const formData = ref()

const dialogRef = inject<{ value: { close: () => void } }>('dialogRef')
const closeDialog = () => {
  dialogRef?.value.close()
}

const addToForm = (values: any) => {
  formData.value = { ...formData.value, ...values }
  console.log(values)
}

const createProject = async () => {

  console.log(formData.value)
  const newProject = {
    id: '',
    name: formData.value.name,
    description: formData.value.description,
    startDate: formData.value.dates[0],
    endDate: formData.value.dates[1],
    color: formData.value.color,
    client: formData.value.client,
    managerId: '',
    createdAt: '',
    employeesIds: formData.value.employees?.map((m: any) => m.id)
  }
  try {
    console.log(newProject)
    await projectsStore.createProject(newProject)
  } catch (error) {
    console.error(error)
    toast.add({
      severity: 'error',
      summary: 'Error creating project',
      life: 3000
    })
    return
  }

  toast.add({
    severity: 'info',
    summary: 'Project created!',
    life: 3000
  })
  closeDialog()
}
</script>

<template>
  <div>
    <Stepper value="1">
      <StepList>
        <Step value="1">{{ t('projects.new.steps.info') }}</Step>
        <Step value="2">{{ t('projects.new.steps.members') }}</Step>
      </StepList>

      <StepPanels>
        <StepPanel v-slot="{ activateCallback }" value="1">
          <CreateProjectMainInfo
            @submit="
              (v) => {
                addToForm(v)
                activateCallback('2')
              }
            "
            @close="closeDialog()"
          />
        </StepPanel>
        <StepPanel v-slot="{ activateCallback }" value="2">
          <CreateProjectMembers
            :forcedDates="formData.dates"
            @submit="
              (v: any) => {
                console.log(v)
                addToForm(v)
                createProject()
              }
            "
            @back="
              () => {
                activateCallback('1')
              }
            "
          />
        </StepPanel>
      </StepPanels>
    </Stepper>
  </div>
</template>
