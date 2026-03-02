<template>
  <v-row>
    <v-col>
      <v-btn-primary
        :prepend-icon="mdiArrowLeft"
        data-test="details-back-button"
        text="Zurück zur Übersicht"
        @click="router.push({ name: ROUTES_HOME })"
      />
    </v-col>
    <v-col cols="auto">
      <zammad-button
        :zammad-nr="antrag.allgemein.zammadTicketNr"
        class="me-5"
      />
      <eakte-button :eakte-coo-adresse="antrag.allgemein.eakteCooAdresse" />
    </v-col>
  </v-row>
</template>

<script lang="ts" setup>
import type { AntragDetails } from "@/types/antragDetails/AntragDetails.ts";

import { mdiArrowLeft } from "@mdi/js";
import { ref, watch } from "vue";

import EakteButton from "@/components/references/EakteButton.vue";
import ZammadButton from "@/components/references/ZammadButton.vue";
import { ROUTES_HOME } from "@/constants.ts";
import router from "@/plugins/router.ts";

const props = defineProps<{ antrag: AntragDetails }>();

const antrag = ref<AntragDetails>(props.antrag);

watch(
  () => props.antrag,
  (newAntrag) => {
    antrag.value = newAntrag;
  }
);
</script>
