<template>
  <v-btn-primary
    :href="eakteToLink(eakteCooAdresse)"
    rel="noopener noreferrer"
    target="_blank"
  >
    E-Akte öffnen
  </v-btn-primary>
</template>

<script lang="ts" setup>
import { eakteToLink } from "@/util/formatter.ts";

const { eakteCooAdresse } = defineProps<{ eakteCooAdresse: string }>();
</script>
