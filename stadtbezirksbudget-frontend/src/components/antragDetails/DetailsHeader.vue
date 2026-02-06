<template>
  <v-row>
    <v-col></v-col>
    <v-col cols="auto">
      <zammad-button
        :zammad-nr="antrag.allgemein.zammadNr"
        class="me-5"
      />
      <eakte-button coo-adresse="a" />
    </v-col>
  </v-row>
</template>

<script lang="ts" setup>
import type { AntragDetails } from "@/types/antragDetails/AntragDetails.ts";

import { ref, watch } from "vue";

import EakteButton from "@/components/references/EakteButton.vue";
import ZammadButton from "@/components/references/ZammadButton.vue";

const props = defineProps<{ antrag: AntragDetails }>();

const antrag = ref<AntragDetails>(props.antrag);

watch(
  () => props.antrag,
  (newAntrag) => {
    antrag.value = newAntrag;
  }
);
</script>
