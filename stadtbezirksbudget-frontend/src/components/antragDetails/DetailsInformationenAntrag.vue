<template>
  <details-card title="Informationen Antrag">
    <v-row>
      <v-col>
        <v-text-field
          v-model="antrag.allgemein.projektTitel"
          data-test="projekt-titel"
          hide-details="auto"
          label="Projekttitel"
          readonly
          variant="outlined"
        />
      </v-col>
      <v-col cols="3">
        <v-date-input
          v-model="antrag.allgemein.eingangDatum"
          data-test="eingang-datum"
          hide-details="auto"
          label="Antragsdatum"
          prepend-icon=""
          readonly
          variant="outlined"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-text-field
          v-model="antrag.allgemein.antragstellerName"
          data-test="antragsteller-name"
          hide-details="auto"
          label="Antragsteller*in"
          readonly
          variant="outlined"
        />
      </v-col>
      <v-col cols="3">
        <v-number-input
          v-model="antrag.allgemein.beantragtesBudget"
          control-variant="hidden"
          data-test="beantragtes-budget"
          hide-details="auto"
          label="Beantragtes Budget"
          readonly
          variant="outlined"
        />
      </v-col>
      <v-col cols="3">
        <v-text-field
          v-model="antrag.allgemein.rubrik"
          data-test="rubrik"
          hide-details="auto"
          label="Rubrik"
          readonly
          variant="outlined"
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <status-select
          v-model="antrag.allgemein.status"
          data-test="status"
          hide-details="auto"
          label="Status"
          readonly
          variant="outlined"
        />
      </v-col>
      <v-col cols="3">
        <v-text-field
          v-model="antrag.allgemein.zammadNr"
          data-test="zammad-nr"
          hide-details="auto"
          label="Zammad-Nummer"
          readonly
          variant="outlined"
        />
      </v-col>
      <v-col cols="3">
        <v-text-field
          v-model="antrag.allgemein.aktenzeichen"
          data-test="aktenzeichen"
          hide-details="auto"
          label="Aktenzeichen"
          readonly
          variant="outlined"
        />
      </v-col>
    </v-row>
    <v-row class="mt-2">
      <v-col class="pt-0 pb-0">
        <v-checkbox
          v-model="antrag.allgemein.istGegendert"
          data-test="ist-gegendert"
          hide-details="auto"
          label="In dem Antrag wurde gegendert"
          readonly
        />
      </v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-textarea
          v-model="antrag.allgemein.anmerkungen"
          auto-grow
          data-test="anmerkungen"
          hide-details="auto"
          label="Anmerkungen"
          readonly
          variant="outlined"
        />
      </v-col>
    </v-row>
  </details-card>
</template>

<script lang="ts" setup>
import type { AntragDetails } from "@/types/antragDetails/AntragDetails.ts";

import { ref, watch } from "vue";

import DetailsCard from "@/components/antragDetails/DetailsCard.vue";
import StatusSelect from "@/components/StatusSelect.vue";

const props = defineProps<{ antrag: AntragDetails }>();

const antrag = ref<AntragDetails>(props.antrag);

watch(
  () => props.antrag,
  (newAntrag) => {
    antrag.value = newAntrag;
  }
);
</script>
