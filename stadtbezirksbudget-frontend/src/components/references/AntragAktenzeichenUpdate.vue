<template>
  <aktenzeichen-edit
    v-model:aktenzeichen="aktenzeichen"
    @update:model-value="updateAktenzeichen"
  />
</template>

<script lang="ts" setup>
import { toRefs } from "vue";

import AktenzeichenEdit from "@/components/references/AktenzeichenEdit.vue";
import { useAntragAktenzeichenUpdate } from "@/composables/useAntragAktenzeichenUpdate.ts";

const props = defineProps<{ antragId: string; initialAktenzeichen: string }>();
const { antragId, initialAktenzeichen } = toRefs(props);
const { updateAktenzeichen, aktenzeichen } = useAntragAktenzeichenUpdate(
  antragId,
  initialAktenzeichen
);
</script>

<style scoped></style>
