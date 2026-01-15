<template>
  <v-btn
    :href="zammadNrToLink(zammadNr)"
    color="primary"
    target="_blank"
    variant="elevated"
  >
    Zammadticket
  </v-btn>
</template>

<script lang="ts" setup>
import { zammadNrToLink } from "@/util/formatter.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
</script>
