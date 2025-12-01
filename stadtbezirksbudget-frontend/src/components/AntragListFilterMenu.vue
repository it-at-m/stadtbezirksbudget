<template>
  <v-menu :close-on-content-click="false">
    <template #activator="{ props }">
      <v-btn
        :icon="mdiFilter"
        aria-label="Anträge filtern"
        data-test="antrag-list-filter-open-btn"
        v-bind="props"
      />
    </template>
    <antrag-list-filter />
  </v-menu>
</template>

<script lang="ts" setup>
import { mdiFilter } from "@mdi/js";

import AntragListFilter from "@/components/AntragListFilter.vue";
</script>
