<template>
  <v-menu
    :close-on-content-click="false"
    width="500"
  >
    <template #activator="{ props }">
      <v-btn
        :icon="mdiFilter"
        :variant="hasActiveFilters ? 'outlined' : undefined"
        aria-label="Anträge filtern"
        data-test="antrag-list-filter-open-btn"
        v-bind="props"
      />
    </template>
    <antrag-list-filter />
  </v-menu>
  <active-filter-notice :active="hasActiveFilters" />
</template>

<script lang="ts" setup>
import { mdiFilter } from "@mdi/js";

import ActiveFilterNotice from "@/components/antragList/ActiveFilterNotice.vue";
import AntragListFilter from "@/components/antragList/AntragListFilter.vue";
import { useAntragListFilter } from "@/composables/useAntragListFilter.ts";

const { hasActiveFilters } = useAntragListFilter();
</script>
