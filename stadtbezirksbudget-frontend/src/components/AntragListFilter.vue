<template>
  <v-menu :close-on-content-click="false">
    <template #activator="{ props }">
      <v-btn
        :icon="mdiFilter"
        v-bind="props"
      />
    </template>
    <v-card>
      <v-card-title>
        <v-row>
          <v-col class="d-flex align-center">Filtern nach</v-col>
          <v-col cols="auto">
            <v-btn
              variant="outlined"
              @click="resetFilters"
              >Alle Filter löschen
            </v-btn>
          </v-col>
        </v-row>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col>
            <status-select
              v-model="filters.status"
              clearable
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
              clearable
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
            <v-date-input
              v-model="filters.eingangDatum"
              clearable
              density="compact"
              hide-details="auto"
              label="Antragsdatum"
              multiple="range"
              prepend-icon=""
              variant="underlined"
              @focusout="updateFilters"
            />
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-autocomplete
              v-model="filters.antragstellerName"
              clearable
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
              clearable
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
            <v-row>
              <v-col>Beantragtes Budget</v-col>
            </v-row>
            <v-row no-gutters>
              <v-col>
                <v-number-input
                  v-model="filters.beantragtesBudgetVon"
                  clearable
                  control-variant="hidden"
                  density="compact"
                  hide-details="auto"
                  label="von"
                  suffix="€"
                  variant="underlined"
                  @focusout="updateFilters"
                />
              </v-col>
              <v-col>
                <v-number-input
                  v-model="filters.beantragtesBudgetBis"
                  clearable
                  control-variant="hidden"
                  density="compact"
                  hide-details="auto"
                  label="bis"
                  suffix="€"
                  variant="underlined"
                  @focusout="updateFilters"
                />
              </v-col>
            </v-row>
          </v-col>
        </v-row>
        <v-row>
          <v-col>
            <v-select
              v-model="filters.art"
              :items="['Fest', 'Fehl']"
              clearable
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
              :items="[
                'Aktualisierung E-Akte',
                'Aktualisierung in Zammad',
                'Aktualisierung Datensatz',
              ]"
              clearable
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
            <v-date-input
              v-model="filters.aktualisierungDatum"
              clearable
              density="compact"
              hide-details="auto"
              label="Datum Aktualisierung"
              multiple="range"
              prepend-icon=""
              variant="underlined"
              @focusout="updateFilters"
            />
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-menu>
</template>

<script lang="ts" setup>
import { mdiFilter } from "@mdi/js";

import StatusSelect from "@/components/StatusSelect.vue";
import { useAntragListFilter } from "@/composables/antragListFilter.ts";

const { updateFilters, resetFilters, filters } = useAntragListFilter();
</script>
