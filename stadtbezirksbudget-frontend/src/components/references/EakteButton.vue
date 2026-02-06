<template>
  <v-btn-primary
    :href="EakteToLink(cooAdresse)"
    rel="noopener noreferrer"
    target="_blank"
  >
    E-Akte öffnen
  </v-btn-primary>
</template>

<script lang="ts" setup>
import { EakteToLink } from "@/util/formatter.ts";

const { cooAdresse } = defineProps<{ cooAdresse: string }>();
</script>
