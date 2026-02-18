<template>
  <a
    :href="eakteLink"
    class="text-black text-decoration-none"
    rel="noopener noreferrer"
    target="_blank"
  >
    {{ aktenzeichen }}
  </a>
</template>

<script lang="ts" setup>
import { useReferenceLink } from "@/composables/useReferenceLink.ts";

const { aktenzeichen, eakteCooAdresse } = defineProps<{
  aktenzeichen: string;
  eakteCooAdresse: string;
}>();
const eakteLink = useReferenceLink().getEakteLink(() => eakteCooAdresse);
</script>

<style scoped>
a:hover {
  text-decoration: underline !important;
}
</style>
