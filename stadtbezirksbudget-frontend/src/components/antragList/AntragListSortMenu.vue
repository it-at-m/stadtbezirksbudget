<template>
  <v-menu :close-on-content-click="false">
    <template #activator="{ props }">
      <v-btn
        :icon="mdiSort"
        aria-label="Anträge sortieren"
        data-test="antrag-list-sort-open-btn"
        v-bind="props"
      />
    </template>
    <antrag-list-sort />
  </v-menu>
</template>

<script lang="ts" setup>
import { mdiSort } from "@mdi/js";

import AntragListSort from "@/components/antragList/AntragListSort.vue";
</script>
