<template>
  <v-menu
    v-model="aktenzeichenMenu"
    :close-on-content-click="false"
    location="end top"
  >
    <v-card
      class="pa-3"
      style="min-width: 260px"
    >
      <div class="text-subtitle-2 mb-2">Aktenzeichen eingeben</div>

      <v-text-field
        v-model="aktenzeichenNumberValue"
        density="compact"
        hide-details
        label="Aktenzeichen"
        type="text"
        @keydown.enter="aktenzeichenSaveNumber"
      />

      <div class="d-flex justify-end ga-2 mt-3">
        <v-btn
          variant="text"
          @click="aktenzeichenMenu = false"
          >Abbrechen</v-btn
        >
        <v-btn
          color="primary"
          @click="aktenzeichenSaveNumber"
          >Speichern</v-btn
        >
      </div>
    </v-card>
    <template #activator="{ props }">
      <v-list-item
        title="Aktenzeichen bearbeiten"
        v-bind="props"
        @click.stop="openAktenzeichen($event, aktenzeichen)"
      >
      </v-list-item>
    </template>
  </v-menu>
</template>

<style scoped></style>

<script lang="ts" setup>
import { ref } from "vue";

import { useEakteList } from "@/composables/useEakteList.ts";

const aktenzeichenMenu = ref(false);

const { aktenzeichen } = defineProps<{
  aktenzeichen: string;
}>();

const aktenzeichenNumberValue = ref<string | null>(null);

const aktenzeichenMenuActivator = ref<HTMLElement | null>(null);

const emit = defineEmits(["update:aktenzeichen"]);
const cooMenu = ref(false);

const cooNumberValue = ref<string | null>(null);

const cooMenuActivator = ref<HTMLElement | null>(null);

const { openAktenzeichen, aktenzeichenSaveNumber } = useEakteList(
  emit,
  aktenzeichenMenu,
  aktenzeichenNumberValue,
  aktenzeichenMenuActivator,
  cooMenu,
  cooNumberValue,
  cooMenuActivator
);
</script>
