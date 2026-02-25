<template>
  <v-card data-test="antrag-list-filter-card">
    <v-card-title>
      <v-row>
        <v-col class="d-flex align-center">Filtern nach</v-col>
        <v-col cols="auto">
          <v-btn-secondary
            data-test="antrag-list-filter-reset-btn"
            @click="resetFilters"
            >Alle Filter löschen
          </v-btn-secondary>
        </v-col>
      </v-row>
    </v-card-title>
    <v-card-text>
      <v-row>
        <v-col>
          <status-select
            v-model="filters.status"
            clearable
            data-test="antrag-list-filter-status"
            density="compact"
            hide-details="auto"
            label="Status"
            multiple
            variant="underlined"
            @focusout="updateFilters"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-autocomplete
            v-model="filters.bezirksausschussNr"
            :items="bezirksausschussNrOptions"
            clearable
            data-test="antrag-list-filter-bezirksausschuss"
            density="compact"
            hide-details="auto"
            label="Bezirk"
            multiple
            variant="underlined"
            @focusout="updateFilters"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <range-input
            v-model:from="filters.eingangDatumVon"
            v-model:to="filters.eingangDatumBis"
            :inputComponent="VDateInput"
            :inputProps="{
              clearable: true,
              density: 'compact',
              hideDetails: 'auto',
              onFocusout: updateFilters,
              prependIcon: '',
              variant: 'underlined',
            }"
            data-test="antrag-list-filter-eingang-datum"
            label="Antragsdatum"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-autocomplete
            v-model="filters.antragstellerName"
            :items="filterOptions.antragstellerNamen"
            clearable
            data-test="antrag-list-filter-antragsteller-name"
            density="compact"
            hide-details="auto"
            label="Antragsteller/in"
            variant="underlined"
            @focusout="updateFilters"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-autocomplete
            v-model="filters.projektTitel"
            :items="filterOptions.projektTitel"
            clearable
            data-test="antrag-list-filter-projekt-titel"
            density="compact"
            hide-details="auto"
            label="Projekt"
            variant="underlined"
            @focusout="updateFilters"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <range-input
            v-model:from="filters.beantragtesBudgetVon"
            v-model:to="filters.beantragtesBudgetBis"
            :inputComponent="VNumberInput"
            :inputProps="{
              clearable: true,
              controlVariant: 'hidden',
              density: 'compact',
              hideDetails: 'auto',
              onFocusout: updateFilters,
              precision: 2,
              suffix: '€',
              variant: 'underlined',
            }"
            data-test="antrag-list-filter-beantragtes-budget"
            label="Budget"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-select
            v-model="filters.finanzierungArt"
            :items="finanzierungArtOptions"
            clearable
            data-test="antrag-list-filter-finanzierung-art"
            density="compact"
            hide-details="auto"
            label="Art"
            variant="underlined"
            @focusout="updateFilters"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <v-select
            v-model="filters.aktualisierungArt"
            :items="aktualisierungArtOptions"
            clearable
            data-test="antrag-list-filter-aktualisierung-art"
            density="compact"
            hide-details="auto"
            label="Aktualisierung"
            multiple
            variant="underlined"
            @focusout="updateFilters"
          />
        </v-col>
      </v-row>
      <v-row>
        <v-col>
          <range-input
            v-model:from="filters.aktualisierungDatumVon"
            v-model:to="filters.aktualisierungDatumBis"
            :inputComponent="VDateInput"
            :inputProps="{
              clearable: true,
              density: 'compact',
              hideDetails: 'auto',
              onFocusout: updateFilters,
              prependIcon: '',
              variant: 'underlined',
            }"
            data-test="antrag-list-filter-aktualisierung-datum"
            label="Datum Aktualisierung"
          />
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { VNumberInput } from "vuetify/components";
import { VDateInput } from "vuetify/labs/components";

import RangeInput from "@/components/common/RangeInput.vue";
import StatusSelect from "@/components/StatusSelect.vue";
import { useAntragListFilter } from "@/composables/useAntragListFilter.ts";
import { useAntragListFilterOptionsStore } from "@/stores/useAntragListFilterOptionsStore.ts";
import { aktualisierungArtOptions } from "@/types/AktualisierungArt.ts";
import { bezirksausschussNrOptions } from "@/types/BezirksausschussNr.ts";
import { finanzierungArtOptions } from "@/types/FinanzierungArt.ts";

const { updateFilters, resetFilters, filters } = useAntragListFilter();
const { filterOptions } = useAntragListFilterOptionsStore();
</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
:deep(.v-field__input) {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
