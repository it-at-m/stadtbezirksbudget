<template>
  <v-card data-test="antrag-list-sort-card">
    <v-card-title>
      <v-row>
        <v-col class="d-flex align-center">Sortieren nach</v-col>
        <v-col cols="auto">
          <v-btn-primary
            data-test="antrag-list-sort-reset-btn"
            @click="resetSorting"
            >Sortierung zurücksetzen
          </v-btn-primary>
        </v-col>
      </v-row>
    </v-card-title>
    <v-card-text>
      <v-row
        v-for="(value, key) in sortDefinitions"
        :key="key"
      >
        <v-col>
          <v-select
            :data-test="value.dataTest"
            :items="sortOptionsByField(key)"
            :label="value.label"
            :model-value="sorting[key]"
            clearable
            density="compact"
            hide-details="auto"
            return-object
            variant="underlined"
            @update:model-value="(v) => (v ? updateSorting(v) : clearSorting())"
          ></v-select>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { useAntragListSort } from "@/composables/useAntragListSort.ts";
import { sortOptionsByField } from "@/types/AntragListSort.ts";
import { sortDefinitions } from "@/types/AntragListSortDefinitions.ts";

const { sorting, resetSorting, clearSorting, updateSorting } =
  useAntragListSort();
</script>
