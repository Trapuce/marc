<script setup lang="ts">
import {
  Button,
  Message,
  InputText,
  DatePicker,
  FloatLabel,
  FileUpload,
  Fieldset,
  MultiSelect,
  type MultiSelectFilterEvent
} from 'primevue'

import { useI18n } from 'vue-i18n'
import { ref, inject } from 'vue'
import { useToast } from 'primevue/usetoast'
import { Form, FormField, type FormSubmitEvent } from '@primevue/forms'
import { z } from 'zod'
import { zodResolver } from '@primevue/forms/resolvers/zod'
import { useEmployeeStore } from '../stores/employees'

const employeeStore = useEmployeeStore()

employeeStore.fetchSkills()

const toast = useToast()
const { t } = useI18n()

const dialogRef = inject<{ value: { close: () => void } }>('dialogRef')
const closeDialog = () => {
  dialogRef?.value.close()
}

const resolver = zodResolver(
  z.object({
    firstName: z.string().min(1, { message: t('employees.new.errors.firstName') }),
    lastName: z.string().min(1, { message: t('employees.new.errors.lastName') }),
    email: z.string().email({ message: t('employees.new.errors.email') }),
    cv: z.preprocess(
      (val) => {
        console.log(val)
        console.log(typeof val)
        if (val === null) {
          return ''
        }
        return val
      },
      z.string().optional()
    ),
    skills: z.preprocess(
      (val) => {
        if (!Array.isArray(val)) {
          return []
        }
        return val.map((skill) => skill.label)
      },
      z.array(z.string()).min(1, {
        message: t('employees.new.errors.skills')
      })
    ),
    experience: z.preprocess(
      (val) => {
        if (typeof val === 'string' && val.trim() !== '') {
          return new Date(val)
        }
        return val
      },
      z.date({ message: t('employees.new.errors.experience') })
    )
  })
)

const uploadedFile = ref<File | null>(null)
function onFileSelect({ files }: { files: File[] }) {
  console.log("onFileSelect", files)
  uploadedFile.value = files[0]
}

const selectedSkills = ref([])
const removeAllSkills = () => {
  selectedSkills.value = []
}

const filterValue = ref('')
const onFilter = (event: MultiSelectFilterEvent) => {
  filterValue.value = event.value
}

const addNewSkill = () => {
  console.log(filterValue.value)
}

const createEmployee = ({ valid, values }: FormSubmitEvent) => {
  if (!valid) return
  console.log('Création de l\'employé:', values)

  try {
    // Créer un objet FormData
    const formData = new FormData()

    // Préparer les données de l'employé en JSON
    const employeeData = {
      firstname: values.firstName,
      lastname: values.lastName,
      email: values.email,
      careerStart: values.experience.toISOString().split('T')[0], // Format YYYY-MM-DD
      skills: selectedSkills.value
    }

    // Ajouter le JSON de l'employé comme string
    formData.append('employee', JSON.stringify(employeeData))

    // Ajouter le fichier CV s'il existe
    if (uploadedFile.value) {
      formData.append('cvFile', uploadedFile.value)
    }

    employeeStore.createEmployee(formData)
    
    toast.add({
      severity: 'success',
      summary: t('employees.new.success.title'),
      detail: t('employees.new.success.message'),
      life: 3000
    })
    
    closeDialog()
  } catch (error) {
    console.error('Erreur lors de la création:', error)
    toast.add({
      severity: 'error',
      summary: t('employees.new.error.title'),
      detail: t('employees.new.error.message'),
      life: 5000
    })
  }
}
</script>

<template>
  <Form :resolver @submit="createEmployee" class="flex flex-col gap-4 p-2 w-full">
    <div class="w-full flex items-center gap-2">
      <FormField v-slot="$field" name="firstName" initialValue="" class="w-full">
        <FloatLabel variant="on">
          <InputText type="text" size="small" class="w-full" />
          <label for="on_label">{{ t('employees.new.firstName') }}</label>
        </FloatLabel>
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
          $field.error?.message
        }}</Message>
      </FormField>

      <FormField v-slot="$field" name="lastName" initialValue="" class="w-full">
        <FloatLabel variant="on">
          <InputText type="text" size="small" class="w-full" />
          <label for="on_label">{{ t('employees.new.lastName') }}</label>
        </FloatLabel>
        <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
          $field.error?.message
        }}</Message>
      </FormField>
    </div>

    <FormField v-slot="$field" name="email" initialValue="" class="w-full">
      <FloatLabel variant="on">
        <InputText type="text" size="small" class="w-full" />
        <label for="on_label">{{ t('employees.new.email') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>

    <FormField v-slot="$field" name="cv" initialValue="" class="w-full">
      <Fieldset :legend="t('employees.new.cv.label')" class="w-full">
        <div class="flex items-center gap-2">
          <FileUpload
            mode="basic"
            @select="onFileSelect"
            customUpload
            auto
            severity="secondary"
            class="p-button-outlined"
            accept=".pdf"
            :chooseLabel="t('employees.new.cv.choose')"
          />
          <p class="m-0">{{ uploadedFile?.name }}</p>
        </div>
      </Fieldset>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>

    <FormField v-slot="$field" name="skills" initialValue="" class="w-full">
      <FloatLabel variant="on">
        <MultiSelect
          v-model="selectedSkills"
          :options="employeeStore.skills"
          optionLabel="label"
          filter
          display="chip"
          class="w-full"
          size="small"
          :maxSelectedLabels="3"
          @filter="onFilter"
        >
          <template #option="slotProps">
            <div class="flex items-center">
              <div>{{ slotProps.option.label }}</div>
            </div>
          </template>
          <template #footer>
            <div class="p-3 flex justify-between">
              <Button
                :label="t('employees.new.skills.add')"
                severity="secondary"
                text
                size="small"
                icon="pi pi-plus"
                @click="addNewSkill"
              />
              <Button
                :label="t('employees.new.skills.remove')"
                severity="danger"
                text
                size="small"
                icon="pi pi-times"
                @click="removeAllSkills"
              />
            </div>
          </template>
        </MultiSelect>
        <label for="on_label">{{ t('employees.new.skills.header') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>

    <FormField v-slot="$field" as="section" name="experience" initialValue="">
      <FloatLabel variant="on">
        <DatePicker
          name="experience"
          fluid
          :manualInput="false"
          size="small"
          v-bind="$field"
          :modelValue="$field.value"
          showButtonBar
        />
        <label for="on_label">{{ t('employees.new.experience') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>

    <div class="flex pt-6 justify-between">
      <Button severity="secondary" :label="t('employees.new.cancel')" @click="closeDialog" />
      <Button :label="t('employees.new.create')" type="submit" />
    </div>
  </Form>
</template>
