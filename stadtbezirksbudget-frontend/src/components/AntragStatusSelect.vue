<template>
  <v-autocomplete
    v-model="status"
    :filter-keys="['raw.shortText', 'raw.longText']"
    :items="statusOptions"
    data-test="antrag-status-select"
    density="compact"
    hide-details="auto"
    item-title="longText"
    variant="plain"
    @update:model-value="updateStatus"
    @update:focused="resetStatus"
  >
    <template #item="{ props, item }">
      <v-list-item
        :subtitle="item.raw.shortText"
        :title="item.raw.longText"
        v-bind="props"
      />
    </template>
    <template #selection="{ item }">
      {{ item.raw.shortText }}
    </template>
  </v-autocomplete>
</template>

<script lang="ts" setup>
import type { Status } from "@/types/Status.ts";

import { useAntragStatusSelect } from "@/composables/antragStatusSelect.ts";

const props = defineProps<{ antragId: string; status: Status }>();
const { updateStatus, resetStatus, status, statusOptions } =
  useAntragStatusSelect(props.antragId, props.status);
</script>
