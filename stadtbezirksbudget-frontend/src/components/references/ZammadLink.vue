<template>
  <a
    :href="zammadNrToLink(zammadNr)"
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

import { zammadNrToLink } from "@/util/formatter.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
</script>

<style scoped>
a:hover {
  text-decoration: underline !important;
}
</style>
