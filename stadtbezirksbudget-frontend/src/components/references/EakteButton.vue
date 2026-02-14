<template>
  <v-btn-primary
    :href="eakteLink"
    rel="noopener noreferrer"
    target="_blank"
  >
    E-Akte öffnen
  </v-btn-primary>
</template>

<script lang="ts" setup>
import { useReferenceLink } from "@/composables/useReferenceLink.ts";

const { eakteCooAdresse } = defineProps<{ eakteCooAdresse: string }>();
const eakteLink = useReferenceLink().getEakteLink(() => eakteCooAdresse);
</script>
