<template>
  <a
    :href="zammadNrToLink(zammadNr)"
    class="text-black text-decoration-none custom-link"
    rel="noopener noreferrer"
    target="_blank"
  >
    {{ zammadNr }}
    <v-icon
      :icon="mdiOpenInNew"
      size="17"
    />
  </a>
</template>

<script lang="ts" setup>
import { mdiOpenInNew } from "@mdi/js";

import { zammadNrToLink } from "@/util/formatter.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
</script>
<style scoped>
.custom-link:hover {
  text-decoration: underline !important;
}
</style>
