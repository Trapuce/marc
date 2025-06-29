<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { PrimeIcons } from '@primevue/core/api'
import { Button } from 'primevue'
import { useDialog } from 'primevue/usedialog'
import ProjectsList from '../components/ProjectsList.vue'
import CreateProjectModal from '../components/CreateProjectModal.vue'

const { t } = useI18n()
const dialog = useDialog()

const createProjectClick = () => {
  dialog.open(CreateProjectModal, {
    props: {
      header: t('projects.new.header'),
      style: {
        width: 'auto',
        minWidth: '35vw',
        maxWidth: '85vw'
      },
      modal: true,
      draggable: false
    }
  })
}
</script>

<template>
  <div>
    <div class="flex items-center justify-between">
      <div class="space-y-2">
        <h1 class="text-2xl font-bold">{{ $t('projects.header') }}</h1>
        <p class="text-sm text-gray-500">{{ $t('projects.sub-header') }}</p>
      </div>
      <Button
        :icon="PrimeIcons.PLUS"
        :label="t('projects.create')"
        size="small"
        @click="createProjectClick"
      />
    </div>
    <div class="mt-4">
      <ProjectsList mode="edit" />
    </div>
  </div>
</template>
