<template>
  <status-select
    v-model="status"
    :search="search"
    data-test="antrag-status-select"
    density="compact"
    hide-details="auto"
    variant="plain"
    @update:model-value="updateStatus"
    @update:focused="toggleStatusAndSearch"
  />
</template>

<script lang="ts" setup>
import type { Status } from "@/types/Status.ts";

import { toRefs } from "vue";

import StatusSelect from "@/components/StatusSelect.vue";
import { useAntragStatusUpdate } from "@/composables/useAntragStatusUpdate.ts";

const props = defineProps<{ antragId: string; initialStatus: Status }>();
const { antragId, initialStatus } = toRefs(props);
const emit = defineEmits<(e: "status-updated", newStatus: Status) => void>();
const onStatusUpdated = (newStatus: Status) =>
  emit("status-updated", newStatus);

const { updateStatus, toggleStatusAndSearch, status, search } =
  useAntragStatusUpdate(antragId, initialStatus, onStatusUpdated);
</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
:deep(.v-field__input) {
  flex-wrap: nowrap;
}
/*noinspection CssUnusedSymbol*/
:deep(.v-autocomplete__selection) {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
