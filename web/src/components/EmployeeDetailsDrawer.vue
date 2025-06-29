<script setup lang="ts">
import { Drawer, Button, Menu, Fieldset, Panel, Tag } from 'primevue'
import { PrimeIcons } from '@primevue/core/api'
import { defineProps, defineEmits, ref } from 'vue'
import type { Employee } from '../models'
import { getExperienceYearsText, formatDateRange } from '../utils/functions'
import { useAuthStore } from '../stores/auth'

const store = useAuthStore()

const props = defineProps<{
  visible: boolean
  employee: Employee
}>()

const emit = defineEmits(['update:visible', 'deleteEmployee'])

const closeDrawer = () => {
  emit('update:visible', false)
}

</script>

<template>
  <Drawer dismissable v-model:visible="props.visible" :header="props.employee?.email" position="right"
    class="!w-full md:!w-[25rem] lg:!w-[40rem]">
    <template #container>
      <div class="flex flex-col h-full justify-between">
        <div class="h-full">
          <div class="flex justify-between p-4">
            <Button type="button" @click="closeDrawer" :icon="PrimeIcons.CHEVRON_RIGHT" size="small" variant="text"
              rounded />
            <Button v-if="store.role === 'hr'" type="button" severity="secondary" :icon="PrimeIcons.TRASH" @click="emit('deleteEmployee', employee)" aria-haspopup="true" size="small" />
          </div>
          <div class="flex flex-col gap-2 overflow-auto p-4 h-[calc(100%-72px)]">
            <h2 class="text-xl font-semibold text-gray-800">{{ props.employee?.firstname }} {{ props.employee?.lastname }}
            </h2>
            <Fieldset :legend="$t('models.employee.email')">
              <p class="m-0">{{ props.employee?.email }}</p>
            </Fieldset>
            <Fieldset :legend="$t('models.employee.experience')">
              <p class="m-0">{{ getExperienceYearsText(props.employee?.careerStart || "", $t('models.employee.year'),
                $t('models.employee.years')) }}</p>
            </Fieldset>
            <Fieldset v-if="props.employee?.urlCV" :legend="$t('models.employee.cv')">
              <Button variant="link" as="a" :href="props.employee.urlCV" target="_blank" :label="props.employee.urlCV" icon-pos="right" icon="pi pi-external-link"
                class="p-0" />
            </Fieldset>
            <Fieldset :legend="$t('models.employee.skills')">
              <Tag v-for="skill in props.employee?.skills" :key="skill" class="mr-2 mb-2" severity="secondary"
                :value="skill" />
            </Fieldset>
            <Fieldset v-if="props.employee?.projects?.length ?? 0 > 0" :legend="$t('models.employee.projects')">
              <div class="flex flex-col gap-2 h-full overflow-auto">
                <Panel toggleable collapsed v-for="project in props.employee?.projects" :key="project.id">
                  <template #header>
                    <div class="flex items-center gap-2">
                      <span class="font-bold"><span :style="{ backgroundColor: project.color }"
                          class="w-4 h-4 rounded-full mr-2 inline-block"></span>{{ project.name }}</span>
                    </div>
                  </template>
                  <div class="description flex items-center gap-2">
                    <label class="font-semibold">{{ $t('models.project.description') }} :</label>
                    <p class="m-0">{{ project.description }}</p>
                  </div>
                  <div class="client flex items-center gap-2">
                    <label class="font-semibold">{{ $t("models.project.client") }} :</label>
                    <span>{{ project.client }}</span>
                  </div>
                  <div class="dates flex items-center gap-2">
                    <label class="font-semibold">{{ $t("models.project.dates") }} :</label>
                    <span>{{ formatDateRange(project.startDate, project.endDate, $t("models.project.from"), $t("models.project.to")) }}</span>
                  </div>
                </Panel>
              </div>
            </Fieldset>
          </div>
        </div>
      </div>
    </template>
  </Drawer>
</template>
