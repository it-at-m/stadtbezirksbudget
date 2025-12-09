<template>
  <v-card data-test="antrag-list-sort-card">
    <v-card-title>
      <v-row>
        <v-col class="d-flex align-center">Sortieren nach</v-col>
        <v-col cols="auto">
          <v-btn
            data-test="antrag-list-sort-reset-btn"
            variant="outlined"
            @click="resetSorting"
            >Sortierung zurücksetzen
          </v-btn>
        </v-col>
      </v-row>
    </v-card-title>
    <v-card-text>
      <v-row
        v-for="{ field, label, dataTest } in sortFields"
        :key="field"
      >
        <v-col>
          <v-select
            :data-test="dataTest"
            :items="sortOptionsByField(field)"
            :label="label"
            :model-value="sortOption[field]"
            clearable
            density="compact"
            hide-details="auto"
            return-object
            variant="underlined"
            @update:model-value="(v) => (v ? updateSorting(v) : resetSorting)"
          ></v-select>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { useAntragListSort } from "@/composables/useAntragListSort.ts";
import { sortOptionsByField } from "@/types/AntragListSort.ts";

const { sortOption, resetSorting, updateSorting } = useAntragListSort();

const sortFields = [
  { field: "status", label: "Status", dataTest: "antrag-list-sort-status" },
  {
    field: "zammadNr",
    label: "Nummer",
    dataTest: "antrag-list-sort-zammad-nr",
  },
  {
    field: "aktenzeichen",
    label: "Aktenzeichen",
    dataTest: "antrag-list-sort-aktenzeichen",
  },
  {
    field: "bezirksausschussNr",
    label: "Bezirk",
    dataTest: "antrag-list-sort-bezirksausschuss-nr",
  },
  {
    field: "eingangDatum",
    label: "Antragsdatum",
    dataTest: "antrag-list-sort-eingang-datum",
  },
  {
    field: "antragstellerName",
    label: "Antragsteller/in",
    dataTest: "antrag-list-sort-antragsteller-name",
  },
  {
    field: "projektTitel",
    label: "Projekt",
    dataTest: "antrag-list-sort-projekt-titel",
  },
  {
    field: "beantragtesBudget",
    label: "Beantragtes Budget",
    dataTest: "antrag-list-sort-beantragtes-budget",
  },
  { field: "istFehlbetrag", label: "Art", dataTest: "antrag-list-sort-art" },
  {
    field: "aktualisierung",
    label: "Aktualisierung",
    dataTest: "antrag-list-sort-aktualisierung",
  },
  {
    field: "aktualisierungDatum",
    label: "Datum Aktualisierung",
    dataTest: "antrag-list-sort-aktualisierung-datum",
  },
];
</script>
