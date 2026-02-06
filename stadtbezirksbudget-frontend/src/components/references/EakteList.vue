<template>
  <v-menu
    v-model="selectMenu"
    :close-on-content-click="false"
    location="bottom end"
  >
    <template #activator="{ props }">
      <a
        :href="EakteToLink(aktenzeichen)"
        class="text-black text-decoration-none"
        rel="noopener noreferrer"
        target="_blank"
        @click.stop
      >
        {{ aktenzeichen }}
      </a>
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
        <aktenzeichen-edit :aktenzeichen></aktenzeichen-edit>
        <v-menu
          v-model="cooMenu"
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
              v-model="cooNumberValue"
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
</template>

<script lang="ts" setup>
import { mdiDotsVertical } from "@mdi/js";
import { ref } from "vue";

import AktenzeichenEdit from "@/components/references/AktenzeichenEdit.vue";
import AntragAktenzeichenUpdate from "@/components/references/AntragAktenzeichenUpdate.vue";
import { useEakteList } from "@/composables/useEakteList.ts";
import { EakteToLink } from "@/util/formatter.ts";

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

const { openCooAdresse, cooSaveNumber } = useEakteList(
  emit,
  aktenzeichenMenu,
  aktenzeichenNumberValue,
  aktenzeichenMenuActivator,
  cooMenu,
  cooNumberValue,
  cooMenuActivator
);
</script>

<style scoped>
a:hover {
  text-decoration: underline !important;
}
</style>
