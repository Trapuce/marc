<script setup lang="ts">
import {
  Button,
  Card,
  Panel,
  IconField,
  InputIcon,
  InputText,
  DataTable,
  Column,
  ConfirmDialog,
  SelectButton,
  Tag,
  Divider,
  Checkbox
} from 'primevue'
import { PrimeIcons } from '@primevue/core/api'
import { ref } from 'vue'
import type { Project } from '../models'
import { useI18n } from 'vue-i18n'
import { useConfirm } from 'primevue/useconfirm'
import { formatDateRange } from '../utils/functions'
import { useProjectStore } from '../stores/projects'
import ProjectDetailsDrawer from './ProjectDetailsDrawer.vue'

const { t } = useI18n()
const props = defineProps<{
  mode: 'edit' | 'select'
}>()

const selectedDisplay = ref('Table')
const displayTypes = ref(['Table', 'Cards'])
const showArchiveProjects = ref(false)
const selectedProjects = ref()
const drawerProject = ref()

const projectsStore = useProjectStore()
projectsStore.fetchProjects(showArchiveProjects.value);

const isDrawerVisible = ref(false)

const emits = defineEmits(['projectSelected'])
const openDetails = (data: Project) => {
  drawerProject.value = data
  isDrawerVisible.value = true
}

const onSelectionChange = (data: Project) => {
  emits('projectSelected', { data })
}

const confirm = useConfirm()
const confirmArchive = (projects: Project[]) => {
  confirm.require({
    header:
      projects.length > 1
        ? t('projects.modal.delete.title-multiple') +
          ' ' +
          projects.length +
          ' ' +
          t('projects.projects')
        : t('projects.modal.delete.title-single') + ' ' + projects[0].name,
    message:
      projects.length > 1
        ? t('projects.modal.delete.message-multiple')
        : t('projects.modal.delete.message-single'),
    accept: () => archiveProjects(projects),
    reject: () => {},
    acceptLabel: t('projects.modal.delete.accept'),
    rejectLabel: t('projects.modal.delete.reject')
  })
}

const archiveProjects = (projects: Project[]) => {
  projects.forEach((project) => {
    projectsStore.archiveProject(project.id)
  })
}

const archiveSelected = () => {
  confirmArchive([drawerProject.value])
  isDrawerVisible.value = false
}

const getRowClass = (project: Project) => {
  return project.archivedAt ? 'opacity-50 group' : 'group'
}
</script>

<template>
  <ProjectDetailsDrawer
    v-if="drawerProject"
    :project="drawerProject"
    v-model:visible="isDrawerVisible"
    @archive="archiveSelected"
  />
  <Card class="mt-4">
    <template #title>
      <div class="flex justify-between">
        <div class="flex items-center gap-2" v-if="props.mode === 'edit'">
          <Checkbox
            v-model="showArchiveProjects"
            binary
            size="small"
            @update:modelValue="projectsStore.fetchProjects(showArchiveProjects)"
          />
          <label for="archive" class="text-sm">Show archive</label>
        </div>
        <SelectButton
          v-model="selectedDisplay"
          :options="displayTypes"
          size="small"
          v-if="props.mode === 'edit'"
        />
      </div>
    </template>
    <template #content v-if="selectedDisplay === 'Table'">
      <DataTable
        :loading="projectsStore.loading"
        :value="projectsStore.projects"
        tableStyle="min-width: 50rem"
        paginator
        scrollable
        dataKey="id"
        :rows="5"
        :rowsPerPageOptions="[5, 10, 20, 50]"
        @update:selection="onSelectionChange"
        v-model:selection="selectedProjects"
        :scrollHeight="'calc(100vh - 350px)'"
        :rowClass="getRowClass"
      >
        <template #empty>{{ t('projects.empty') }}</template>
        <template #paginatorstart>
          <Button type="button" icon="pi pi-refresh" text @click="projectsStore.fetchProjects(showArchiveProjects);" />
        </template>
        <Column
          :selectionMode="props.mode === 'select' ? 'single' : 'multiple'"
          headerStyle="width: 3rem"
        ></Column>
        <Column :header="$t('models.project.name')" field="name">
          <template #body="slotProps">
            <div class="flex items-center">
              <i v-if="slotProps.data.archivedAt" class="pi pi-inbox mr-2" />
              <span
                v-else
                :style="{ backgroundColor: slotProps.data.color }"
                class="min-w-4 max-w-4 w-4 min-h-4 max-h-4 h-4 rounded-full mr-2"
              ></span>
              {{ slotProps.data.name }}
            </div>
          </template>
        </Column>
        <Column field="employeesIds" :header="$t('models.project.collaborators')">
          <template #body="slotProps">
            <div class="flex flex-wrap gap-2">
              <Tag
                severity="secondary"
                :value="slotProps.data.employeesIds.length"
              />
            </div>
          </template>
        </Column>
        <Column :header="$t('models.project.dates')">
          <template #body="slotProps">
            <div class="flex items-center whitespace-nowrap">
              {{
                formatDateRange(
                  slotProps.data.startDate,
                  slotProps.data.endDate,
                  t('models.project.from'),
                  t('models.project.to')
                )
              }}
            </div>
          </template>
        </Column>
        <Column v-if="props.mode === 'edit'">
          <template #header v-if="!showArchiveProjects">
            <div class="flex justify-end gap-2 w-full">
              <Button
                @click="confirmArchive(selectedProjects)"
                :label="$t('projects.archive')"
                icon="pi pi-inbox"
                iconPos="left"
                severity="secondary"
                size="small"
                :disabled="!selectedProjects || selectedProjects.length === 0"
                v-tooltip.top="{ value: 'Select projects to archive', autoHide: true }"
              />
            </div>
          </template>
          <template #body="{ data }">
            <div
              class="flex gap-2 justify-end opacity-0 group-hover:opacity-100 transition-opacity duration-100"
            >
              <Button
                @click="() => openDetails(data)"
                :label="$t('projects.details')"
                icon="pi pi-eye"
                iconPos="left"
                severity="secondary"
                size="small"
              />
              <Button
                v-if="!data.archivedAt"
                @click="() => confirmArchive([data])"
                :label="$t('projects.archive')"
                icon="pi pi-inbox"
                iconPos="left"
                severity="secondary"
                size="small"
              />
            </div>
          </template>
        </Column>
      </DataTable>
    </template>
    <template #content v-else-if="mode === 'edit'">
      <div class="mt-4 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 overflow-auto">
        <Panel
          v-for="project in projectsStore.projects"
          :key="project.name"
          @click="openDetails(project)"
          class="cursor-pointer"
        >
          <h3 class="text-xl font-bold">{{ project.name }}</h3>
          <Divider />
          <div class="mt-2">
            <div class="flex items-center gap-2">
              <i :class="PrimeIcons.USER" />
              <div class="flex flex-wrap items-center gap-2 mt-2">
                <Tag
                  severity="secondary"
                  v-for="employee in project.employees"
                  :key="employee.id"
                  :value="employee.firstname + ' ' + employee.lastname"
                />
              </div>
            </div>
          </div>
          <div class="flex items-center gap-2 mt-4">
            <i :class="PrimeIcons.CALENDAR_MINUS" />
            <span>{{ project.startDate }}</span>
            <span> - </span>
            <span>{{ project.endDate }}</span>
          </div>
        </Panel>
      </div>
    </template>
  </Card>
  <ConfirmDialog></ConfirmDialog>
</template>
