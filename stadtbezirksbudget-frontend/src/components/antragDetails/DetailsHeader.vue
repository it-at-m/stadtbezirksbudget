<template>
  <v-row>
    <v-col></v-col>
    <v-col cols="auto">
      <zammad-button :zammad-nr="antrag.zammadNr" />
    </v-col>
  </v-row>
</template>

<script lang="ts" setup>
import type { AntragDetails } from "@/types/AntragDetails.ts";

import { ref, watch } from "vue";

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
