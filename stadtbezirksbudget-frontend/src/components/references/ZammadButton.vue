<template>
  <v-btn-primary
    :href="zammadNrToLink(zammadNr)"
    rel="noopener noreferrer"
    target="_blank"
  >
    Zammad Ticket
  </v-btn-primary>
</template>

<script lang="ts" setup>
import { zammadNrToLink } from "@/util/formatter.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
</script>
