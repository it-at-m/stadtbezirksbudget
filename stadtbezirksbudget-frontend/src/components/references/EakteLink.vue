<template>
  <a
    :href="eakteToLink(eakteCooAdresse)"
    class="text-black text-decoration-none"
    rel="noopener noreferrer"
    target="_blank"
  >
    {{ aktenzeichen }}
  </a>
</template>

<script lang="ts" setup>
import { eakteToLink } from "@/util/formatter.ts";

const { aktenzeichen, eakteCooAdresse } = defineProps<{
  aktenzeichen: string;
  eakteCooAdresse: string;
}>();
</script>

<style scoped>
a:hover {
  text-decoration: underline !important;
}
</style>
