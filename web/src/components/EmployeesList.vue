<script setup lang="ts">
import {
  Button,
  SplitButton,
  Card,
  DataTable,
  Column,
  ConfirmDialog,
  MultiSelect,
  TreeSelect,
  Tag,
  DatePicker
} from 'primevue'
import SliderDropdown from '../components/SliderDropdown.vue'
import { useI18n } from 'vue-i18n'
import { useAuthStore } from '../stores/auth'
import { useConfirm } from 'primevue/useconfirm'
import { computed, onMounted, ref, watch } from 'vue'
import { type Employee, type SearchProfile } from '../models'
import { getExperienceYears, getExperienceYearsText } from '../utils/functions'
import SavePresetButton from './SavePresetButton.vue'
import { useSearchProfileStore } from '../stores/search'
import { useEmployeeStore } from '../stores/employees'
const { t } = useI18n()
const authStore = useAuthStore()
const searchProfileStore = useSearchProfileStore();
searchProfileStore.getAllSearchProfiles()

const employeeStore = useEmployeeStore()
employeeStore.fetchSkills()

const props = defineProps<{
  editMode?: boolean;
  forcedDates?: Date[];
}>()
   
const onSelectionChange = (e: Employee[] ) => {
  emit('updateSelectedEmployees', { employees: e })
}

const selectedExperience = ref(0)
const selectedSkills = ref<string[]>([])
const selectedDates = ref<Date[]>([])
const selectedEmployees = ref()

const selectedPreset = ref(null)
const selectedPresetObject = ref<SearchProfile>({
  id: '',
  name: '',
  createdAt: "",
  experience: 0,
  skillsIds: [],
  startDate: "",
  endDate: ""
})

const updateSelectedDates = (newDates: Date | Date[] | (Date | null)[] | null | undefined) => {
  if (!Array.isArray(newDates) || newDates.length < 2) {
    console.warn("Dates invalides :", newDates)
    return
  }

  // Assure-toi que ce sont bien des `Date`
  const validDates = newDates.filter(date => date instanceof Date) as Date[]

  if (validDates.length < 2) {
    console.warn("Plage de dates incomplète :", validDates)
    return
  }
  
  selectedDates.value = validDates
}


const formatDate = (date: Date) => {
  return date?.toISOString().split("T")[0];
}
const fetchEmployees = () => {
  employeeStore.fetchEmployees("", "", selectedSkills.value, selectedExperience.value, formatDate(selectedDates.value[0]), formatDate(selectedDates.value[1]));
}

let isUpdatingFromPreset = false
let isResettingPreset = false

const isDefaultFilter = computed(() => {
  return (
    selectedExperience.value === 0 &&
    selectedSkills.value.length === 0 &&
    selectedDates.value.length === 0
  )
})

const isNewFilter = computed(() => {
  return !isDefaultFilter.value && selectedPreset.value === null
})

const confirm = useConfirm()
const confirmDelete = (employees: Employee[]) => {
  confirm.require({
    header:
      employees.length > 1
        ? t('employees.modal.delete.title-multiple') +
        ' ' +
        employees.length +
        ' ' +
        t('employees.employees')
        : t('employees.modal.delete.title-single') +
        ' ' +
        employees[0].firstname +
        ' ' +
        employees[0].lastname,
    message:
      employees.length > 1
        ? t('employees.modal.delete.message-multiple')
        : t('employees.modal.delete.message-single'),
    accept: () => deleteEmployees(employees),
    reject: () => {},
    acceptLabel: t('employees.modal.delete.accept'),
    rejectLabel: t('employees.modal.delete.reject')
  })
}

const deleteEmployees = (employees: Employee[]) => {
  selectedEmployees.value.filter((emp: Employee) => !employees.includes(emp))
  console.log('Delete employees')
}

const emit = defineEmits<{
  (event: 'openDetails', data: Employee): void
  (event: 'addEmployeesToProject', data: Employee[]): void
  (event: 'updateSelectedEmployees', data: { employees: Employee[]}): void
}>()

const openDetails = (data: Employee) => {
  console.log(data)
  emit('openDetails', data)
}

const addEmployeesToProject = (data: Employee[]) => {
  emit('addEmployeesToProject', data)
}

function getMenuItems(data: Employee) {
  if (authStore.role === 'hr') {
    return [
      {
        label: t('employees.delete'),
        icon: 'pi pi-trash',
        command: () => {
          confirmDelete([data])
        }
      }
    ]
  } else {
    return [
      {
        label: t('projects.add-to-project'),
        icon: 'pi pi-plus',
        command: () => {
          addEmployeesToProject([data])
        }
      }
    ]
  }
}

// format : {1:true}
// 1 is the id of the selected preset
watch(selectedPreset, (key) => {
  // Activation du flag pour éviter la boucle infinie
  isUpdatingFromPreset = true;

  if (isResettingPreset) {
    isResettingPreset = false;
    return;
  }

  if (!key) {
    resetPresetObject();
    isUpdatingFromPreset = false;
    return console.log('No preset selected');
  }

  const id = Object.keys(key)[0];
  const preset = searchProfileStore.searchProfiles.find((profile) => profile.id === id);

  if (preset) {
    console.log('preset found', preset);
    selectedPresetObject.value = preset;
    selectedExperience.value = preset.experience;
    selectedSkills.value = preset.skillsIds;
    selectedDates.value = preset.startDate
      ? preset.endDate
        ? [new Date(preset.startDate), new Date(preset.endDate)]
        : [new Date(preset.startDate)]
      : [];
  } else {
    console.log('Preset not found');
    resetPresetObject();
  }

  isUpdatingFromPreset = false;
});

// Changements manuels des filtres
watch([selectedExperience, selectedSkills, selectedDates], () => { 
  console.log('Watch triggered', { 
    isUpdatingFromPreset, 
    selectedPreset: selectedPreset.value, 
    selectedExperience: selectedExperience.value, 
    selectedSkills: selectedSkills.value, 
    selectedDates: selectedDates.value 
  });

  if (!isUpdatingFromPreset) {
    console.log('Resetting preset');
    isResettingPreset = true;
    selectedPreset.value = null;

    selectedPresetObject.value = {
      id: selectedPresetObject.value?.id || '',
      name: selectedPresetObject.value?.name || '',
      createdAt: selectedPresetObject.value?.createdAt || '',
      experience: selectedExperience.value,
      skillsIds: [...selectedSkills.value],
      startDate: selectedDates.value[0]?.toISOString() || '',
      endDate: selectedDates.value[1]?.toISOString() || ''
    };
  }

  console.log('Fetching employees...');
  fetchEmployees();
});


onMounted(() => {
  fetchEmployees();
});

function resetPresetObject() {
  selectedPresetObject.value = {
    id: '',
    name: '',
    createdAt: '',
    experience: 0,
    skillsIds: [],
    startDate: '',
    endDate: ''
  };
  selectedExperience.value = 0;
  selectedSkills.value = [];
  selectedDates.value = [];
}

watch(() => props.forcedDates, (newDates) => {
  if (newDates) {
    selectedDates.value = newDates
  }
}, { immediate: true })

</script>

<template>
  <Card class="mt-4 min-w-full">
    <template #title>
      <div class="flex items-center gap-2">
        <SliderDropdown v-model="selectedExperience" />
        <MultiSelect v-model="selectedSkills" :options="employeeStore.skills" optionValue="id" optionLabel="label"
          :placeholder="t('employees.filters.skills')" :maxSelectedLabels="3" filter size="small" />
        <DatePicker fluid selectionMode="range" :manualInput="false" :numberOfMonths="2" size="small"
          :modelValue="selectedDates" @update:model-value="updateSelectedDates" showButtonBar :placeholder="t('employees.filters.date-range')" :disabled="forcedDates && forcedDates.length > 0" />
        <div class="flex gap-2 items-center" v-if="authStore.role === 'manager'">
          <TreeSelect v-model="selectedPreset"
            :options="searchProfileStore.searchProfiles.map(profile => ({ key: profile.id, label: profile.name, data: profile }))"
            :placeholder="t('employees.filters.presets')" filter size="small" />
          <SavePresetButton :preset="selectedPresetObject" :disabled="!isNewFilter" />
        </div>
      </div>
    </template>
    <template #content>
      <DataTable
        class="min-w-full md:min-w-0"
        @update:selection="onSelectionChange"
        v-model:selection="selectedEmployees"
        :loading="employeeStore.loading"
        :value="employeeStore.employees"
        dataKey="id"
        paginator
        :rows="5"
        :rowsPerPageOptions="[5, 10, 20, 50]"
        scrollable
        :scrollHeight="props.editMode ? 'calc(100vh - 350px)' : 'full'"
        :row-class="() => 'group'"
      >
        <template #paginatorstart>
          <Button type="button" icon="pi pi-refresh" text @click="employeeStore.fetchEmployees()" />
        </template>
        <Column selectionMode="multiple" selectAll headerStyle="width: 3rem"></Column>
        <Column :header="t('models.employee.first-lastname')">
          <template #body="slotProps">
            {{ slotProps.data.firstname }} {{ slotProps.data.lastname }}
          </template>
        </Column>
        <Column :header="t('models.employee.experience')">
          <template #body="slotProps">
            {{
              getExperienceYearsText(
                slotProps.data.careerStart,
                $t('models.employee.year'),
                $t('models.employee.years')
              )
            }}
          </template>
        </Column>
        <Column :header="t('models.employee.skills')">
          <template #body="slotProps">
            <div class="flex flex-wrap gap-2">
              <Tag
                severity="secondary"
                v-for="skill in slotProps.data.skills"
                :key="skill"
                :value="skill"
              />
            </div>
          </template>
        </Column>
        <Column v-if="props.editMode">
          <template #header>
            <div class="flex justify-end gap-2 w-full">
              <Button :label="t('projects.add-to-project')" icon="pi pi-plus" iconPos="left" severity="secondary"
                size="small" :disabled="!selectedEmployees || selectedEmployees.length === 0"
                v-tooltip.top="{ value: 'Select employees to add to the project', autoHide: true }"
                @click="() => addEmployeesToProject(selectedEmployees)" v-if="authStore.role === 'manager'" />
              <Button @click="confirmDelete(selectedEmployees)" :label="t('employees.delete')" severity="secondary" icon="pi pi-trash"
                iconPos="left" size="small" :disabled="!selectedEmployees || selectedEmployees.length === 0"
                v-tooltip.top="{ value: 'Select employees to delete', autoHide: true }"
                v-if="authStore.role === 'hr'" />
            </div>
          </template>
          <template #body="{ data }">
            <div class="flex gap-2 justify-end opacity-0 group-hover:opacity-100 transition-opacity duration-100">
              <SplitButton label="Details" icon="pi pi-eye" severity="secondary" outlined
                dropdownIcon="pi pi-ellipsis-v" @click="() => openDetails(data)" :model="getMenuItems(data)"
                size="small" />
            </div>
          </template>
        </Column>
      </DataTable>
    </template>
  </Card>

  <ConfirmDialog></ConfirmDialog>
</template>
