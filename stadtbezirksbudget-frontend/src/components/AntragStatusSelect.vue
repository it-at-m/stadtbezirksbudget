<template>
  <v-autocomplete
    v-model="status"
    :filter-keys="['raw.shortText', 'raw.longText']"
    :items="statusOptions"
    :search="search"
    data-test="antrag-status-select"
    density="compact"
    hide-details="auto"
    item-title="shortText"
    variant="plain"
    @update:model-value="updateStatus"
    @update:focused="resetStatus"
  >
    <template #item="{ props, item }">
      <v-list-item
        :subtitle="item.raw.longText"
        :title="item.raw.shortText"
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
const { updateStatus, resetStatus, status, search, statusOptions } =
  useAntragStatusSelect(props.antragId, props.status);
</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
:deep(.v-field__input) {
  flex-wrap: nowrap;
}
</style>
