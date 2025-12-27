<template>
  <a
    :href="zammadNrToLink(zammadNr)"
    rel="noopener noreferrer"
    target="_blank"
  >
    {{ zammadNr }}
  </a>
</template>

<script lang="ts" setup>
import { zammadNrToLink } from "@/util/formatter.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
</script>
