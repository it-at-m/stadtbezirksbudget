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
    item-value="value"
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

import { toRefs } from "vue";

import { useAntragStatusSelect } from "@/composables/antragStatusSelect.ts";

const props = defineProps<{ antragId: string; initialStatus: Status }>();
const { antragId, initialStatus } = toRefs(props);
const { updateStatus, resetStatus, status, search, statusOptions } =
  useAntragStatusSelect(antragId, initialStatus);
</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
:deep(.v-field__input) {
  flex-wrap: nowrap;
}
</style>
