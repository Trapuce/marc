<script setup lang="ts">
import { Drawer, Button, Fieldset, Panel, Tag } from 'primevue'
import { PrimeIcons } from '@primevue/core/api'
import { defineProps, defineEmits, ref, onMounted, watch } from 'vue'
import { type Project, type Employee } from '../models'
import { getExperienceYearsText, formatDateRange } from '../utils/functions'
import { useEmployeeStore } from '../stores/employees'

const props = defineProps<{
  visible: boolean
  project: Project
}>()

const emit = defineEmits(['update:visible', 'archive'])

const employeeStore = useEmployeeStore()
const employees = ref<Employee[]>([])

const fetchEmployees = async () => {
  if (!props.project.employeesIds) {
    return
  }
  employees.value = []
  props.project.employeesIds?.forEach(async (id: string) => {
    const employeeData = await employeeStore.fetchEmployeeById(id)
    employees.value.push(employeeData)
    console.log(employees.value)
  })
}

const closeDrawer = () => {
  emit('update:visible', false)
}

watch(() => props.visible, async (newVal) => {
  if (newVal) {
    await fetchEmployees()
  }
})

onMounted(async () => {
  await fetchEmployees()
})
</script>

<template>
  <Drawer
    dismissable
    v-model:visible="props.visible"
    :header="props.project?.name"
    position="right"
    class="!w-full md:!w-[25rem] lg:!w-[40rem]"
  >
    <template #container>
      <div class="flex flex-col h-full justify-between">
        <div class="h-full">
          <div class="flex justify-between p-4">
            <Button
              type="button"
              @click="closeDrawer"
              :icon="PrimeIcons.CHEVRON_RIGHT"
              size="small"
              variant="text"
              rounded
            />
            <Button
              v-if="!props.project.archivedAt"
              @click="emit('archive')"
              :label="$t('projects.archive')"
              icon="pi pi-inbox"
              iconPos="left"
              severity="secondary"
              size="small"
            />
          </div>
          <div class="flex flex-col gap-2 overflow-auto p-4 h-[calc(100%-72px)]">
            <h2 class="text-xl font-semibold text-gray-800">
              <span
                :style="{ backgroundColor: props.project.color }"
                class="w-4 h-4 rounded-full mr-2 inline-block"
              ></span
              >{{ props.project.name }}
            </h2>
            <Fieldset :legend="$t('models.project.description')">
              <p class="m-0">{{ props.project?.description }}</p>
            </Fieldset>
            <div class="flex gap-2">
              <Fieldset class="w-1/2" :legend="$t('models.project.client')">
                <p class="m-0">{{ props.project?.client }}</p>
              </Fieldset>
              <Fieldset class="w-1/2" :legend="$t('models.project.dates')">
                <p class="m-0">
                  {{
                    formatDateRange(
                      props.project.startDate,
                      props.project.endDate,
                      $t('models.project.from'),
                      $t('models.project.to')
                    )
                  }}
                </p>
              </Fieldset>
            </div>
            <Fieldset :legend="$t('models.project.collaborators')">
              <p v-if="employees.length <= 0">{{ $t('projects.no-employees') }}</p>
              <div class="flex flex-col gap-2 h-full overflow-auto">
                <Panel
                  toggleable
                  collapsed
                  v-for="employee in employees"
                  :key="employee.id"
                >
                  <template #header>
                    <div class="flex items-center gap-2">
                      <span class="font-bold">{{ employee.firstname }} {{ employee.lastname }}</span
                      ><span class="text-grey text-sm">{{ employee.email }}</span>
                    </div>
                  </template>
                  <div class="skills flex items-center gap-2">
                    <label class="font-semibold">{{ $t('models.employee.skills') }} :</label>
                    <Tag
                      severity="secondary"
                      v-for="skill in employee.skills"
                      :key="skill"
                      :value="skill"
                    />
                  </div>
                  <div class="experience flex items-center gap-2">
                    <label class="font-semibold">{{ $t('models.employee.experience') }} :</label>
                    <span>{{
                      getExperienceYearsText(
                        employee.careerStart,
                        $t('models.employee.year'),
                        $t('models.employee.years')
                      )
                    }}</span>
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
