<template>
  <v-btn
    :href="zammadNrToLink(zammadNr)"
    color="primary"
    rel="noopener noreferrer"
    target="_blank"
    variant="elevated"
  >
    Zammad Ticket
  </v-btn>
</template>

<script lang="ts" setup>
import { zammadNrToLink } from "@/util/formatter.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
</script>
