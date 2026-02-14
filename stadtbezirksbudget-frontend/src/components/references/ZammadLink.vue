<template>
  <a
    :href="zammadLink"
    class="text-black text-decoration-none d-flex align-center ga-1"
    rel="noopener noreferrer"
    target="_blank"
  >
    {{ zammadNr }}
    <v-icon
      :icon="mdiOpenInNew"
      size="16"
    />
  </a>
</template>

<script lang="ts" setup>
import { mdiOpenInNew } from "@mdi/js";

import { useReferenceLink } from "@/composables/useReferenceLink.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
const zammadLink = useReferenceLink().getZammadLink(() => zammadNr);
</script>

<style scoped>
a:hover {
  text-decoration: underline !important;
}
</style>
