<template>
  <v-container>
    <v-menu
      v-model="selectMenu"
      :close-on-content-click="false"
      location="bottom end"
    >
      <template #activator="{ props }">
        {{ aktenzeichen }}
        <v-btn
          elevation="0"
          height="25"
          icon
          v-bind="props"
          width="25"
          @click.stop
        >
          <v-icon :icon="mdiDotsVertical"> </v-icon>
        </v-btn>
      </template>

      <v-card>
        <v-list>
          <v-menu
            v-model="aktenzeichenMenu"
            :activator="aktenzeichenMenuActivator"
            :close-on-content-click="false"
            location="end top"
            open-on-click
          >
            <v-card
              class="pa-3"
              style="min-width: 260px"
            >
              <div class="text-subtitle-2 mb-2">Aktenzeichen eingeben</div>

              <v-text-field
                v-model.number="aktenzeichenNumberValue"
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
                @click="openAktenzeichen"
              >
              </v-list-item>
            </template>
          </v-menu>
          <v-menu
            v-model="cooMenu"
            :activator="cooMenuActivator"
            :close-on-content-click="false"
            location="end"
            open-on-click
          >
            <v-card
              class="pa-3"
              style="min-width: 260px"
            >
              <div class="text-subtitle-2 mb-2">COO-Adresse eingeben</div>

              <v-text-field
                v-model.number="cooNumberValue"
                density="compact"
                hide-details
                label="COO-Adresse"
                type="text"
                @keydown.enter="cooSaveNumber"
              />

              <div class="d-flex justify-end ga-2 mt-3">
                <v-btn
                  variant="text"
                  @click="cooMenu = false"
                  >Abbrechen</v-btn
                >
                <v-btn
                  color="primary"
                  @click="cooSaveNumber"
                  >Speichern</v-btn
                >
              </div>
            </v-card>
            <template #activator="{ props }">
              <v-list-item
                title="COO-Adresse bearbeiten"
                v-bind="props"
                @click="openCooAdresse"
              >
              </v-list-item>
            </template>
          </v-menu>
        </v-list>
      </v-card>
    </v-menu>
  </v-container>
</template>

<script lang="ts" setup>
import { mdiDotsVertical } from "@mdi/js";
import { ref } from "vue";

import { useEakteList } from "@/composables/useEakteList.ts";

const selectMenu = ref(false);
const aktenzeichenMenu = ref(false);

const { aktenzeichen, cooAdresse } = defineProps<{
  aktenzeichen: string;
  cooAdresse: string;
}>();

const aktenzeichenNumberValue = ref<string | null>(null);

const aktenzeichenMenuActivator = ref<HTMLElement | null>(null);

const emit = defineEmits(["update:aktenzeichen, update:cooAdresse"]);
const cooMenu = ref(false);

const cooNumberValue = ref<string | null>(null);

const cooMenuActivator = ref<HTMLElement | null>(null);

const {
  openAktenzeichen,
  aktenzeichenSaveNumber,
  openCooAdresse,
  cooSaveNumber,
} = useEakteList(
  emit,
  aktenzeichenMenu,
  aktenzeichenNumberValue,
  aktenzeichenMenuActivator,
  cooMenu,
  cooMenuActivator,
  cooNumberValue
);
</script>

<style scoped></style>
