<template>
  <v-autocomplete
    v-model="status"
    :items="statusOptions"
    density="compact"
    hide-details="auto"
    variant="plain"
    @update:model-value="updateStatus"
  />
</template>

<script lang="ts" setup>
import type { Status } from "@/types/Status.ts";

import { useAntragStatusSelect } from "@/composables/antragStatusSelect.ts";

const props = defineProps<{ antragId: string; status: Status }>();
const { updateStatus, status, statusOptions } = useAntragStatusSelect(
  props.antragId,
  props.status
);
</script>
