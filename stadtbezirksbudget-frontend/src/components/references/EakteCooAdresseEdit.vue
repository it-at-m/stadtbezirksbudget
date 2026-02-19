<template>
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
    location="bottom end"
  >
    <template #activator="{ props }">
      <v-list-item v-bind="props">COO-Adresse bearbeiten</v-list-item>
    </template>
    <v-card>
      <v-card-title>COO-Adresse bearbeiten</v-card-title>
      <v-card-text>
        <v-form
          v-model="isValid"
          @submit.prevent
        >
          <v-text-field
            v-model="eakteCooAdresse"
            :rules="[validateCoo]"
            hide-details="auto"
            label="COO-Adresse"
          />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-btn-secondary @click="cancel">Abbrechen</v-btn-secondary>
        <v-btn-primary
          :disabled="!isSaveable"
          @click="save"
        >
          Speichern
        </v-btn-primary>
      </v-card-actions>
    </v-card>
  </v-menu>
</template>

<script lang="ts" setup>
import { useEakteCooAdresseEdit } from "@/composables/useEakteCooAdresseEdit.ts";

const props = defineProps<{
  antragId: string;
  eakteCooAdresse: string;
}>();
const emit = defineEmits<(e: "save") => void>();
const onSave = () => emit("save");
const {
  menu,
  eakteCooAdresse,
  isValid,
  isSaveable,
  save,
  cancel,
  validateCoo,
} = useEakteCooAdresseEdit(
  () => props.antragId,
  () => props.eakteCooAdresse,
  onSave
);
</script>
