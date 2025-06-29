<script setup lang="ts">
import {
  Button,
  Message,
  InputText,
  Textarea,
  ColorPicker,
  DatePicker,
  FloatLabel,
  InputGroup,
  InputGroupAddon
} from 'primevue'
import { PrimeIcons } from '@primevue/core/api'
import { Form, FormField, type FormSubmitEvent } from '@primevue/forms'
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { z } from 'zod'
import { zodResolver } from '@primevue/forms/resolvers/zod'

const { t } = useI18n()
const color = ref('')

const resolver = zodResolver(
  z.object({
    name: z.string().min(1, { message: t('projects.new.errors.name') }),
    description: z.string().min(1, { message: t('projects.new.errors.description') }),
    client: z.string().optional(),
    dates: z.preprocess(
      (val) => {
        if (!Array.isArray(val) || val.length !== 2) {
          return null
        }
        return val.map((date) => (date ? new Date(date) : null))
      },
      z.tuple([
        z
          .date()
          .nullable()
          .refine((val) => val !== null, { message: t('projects.new.errors.dates.start') }),
        z
          .date()
          .nullable()
          .refine((val) => val !== null, { message: t('projects.new.errors.dates.end') })
      ])
    ),
    color: z
      .union([z.string().min(1, { message: t('projects.new.errors.color') }), z.literal(null)])
      .refine((value) => value !== null, { message: t('projects.new.errors.color') })
  })
)

const emit = defineEmits(['close', 'submit'])
const onFormSubmit = ({ valid, values }: FormSubmitEvent) => {
  if (valid) {
    emit('submit', values)
  }
}
</script>

<template>
  <Form :resolver @submit="onFormSubmit" class="flex flex-col gap-4 p-2 w-full">
    <FormField v-slot="$field" as="section" name="name" initialValue="">
      <FloatLabel variant="on">
        <InputText type="text" size="small" class="w-full" />
        <label for="on_label">{{ t('projects.new.name') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>
    <FormField v-slot="$field" as="section" name="description" initialValue="">
      <FloatLabel variant="on">
        <Textarea autoResize rows="5" cols="30" size="small" class="w-full" />
        <label for="on_label">{{ t('projects.new.description') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>
    <FormField v-slot="$field" as="section" name="client" initialValue="">
      <FloatLabel variant="on">
        <InputText type="text" size="small" class="w-full" />
        <label for="on_label">{{ t('projects.new.client') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>
    <FormField v-slot="$field" as="section" name="dates" initialValue="">
      <FloatLabel variant="on">
        <DatePicker
          name="dates"
          fluid
          selectionMode="range"
          :manualInput="false"
          :numberOfMonths="2"
          size="small"
          v-bind="$field"
          :modelValue="$field.value ?? []"
          showButtonBar
        />
        <label for="on_label">{{ t('projects.new.dates') }}</label>
      </FloatLabel>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>
    <FormField v-slot="$field" as="section" name="color" initialValue="">
      <InputGroup>
        <InputGroupAddon>
          <ColorPicker name="color" format="hex" v-model="color" />
        </InputGroupAddon>
        <FloatLabel variant="on">
          <InputText type="text" size="small" class="w-full" v-model="color" />
          <label for="on_label">{{ t('projects.new.color') }} (hex)</label>
        </FloatLabel>
      </InputGroup>
      <Message v-if="$field?.invalid" severity="error" size="small" variant="simple">{{
        $field.error?.message
      }}</Message>
    </FormField>
    <div class="flex pt-6 justify-between">
      <Button severity="secondary" label="Cancel" @click="emit('close')" />
      <Button label="Next" :icon="PrimeIcons.ARROW_RIGHT" iconPos="right" type="submit" />
    </div>
  </Form>
</template>
