<template>
  <status-select
    v-model="status"
    :search="search"
    data-test="antrag-status-select"
    density="compact"
    hide-details="auto"
    variant="plain"
    @update:model-value="updateStatus"
    @update:focused="onFocusChange"
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

const { updateStatus, onFocusChange, status, search } = useAntragStatusUpdate(
  antragId,
  initialStatus,
  onStatusUpdated
);
</script>
