<template>
  <v-btn-primary
    :href="zammadLink"
    rel="noopener noreferrer"
    target="_blank"
  >
    Zammad Ticket
  </v-btn-primary>
</template>

<script lang="ts" setup>
import { useReferenceLink } from "@/composables/useReferenceLink.ts";

const { zammadNr } = defineProps<{ zammadNr: string }>();
const zammadLink = useReferenceLink().getZammadLink(() => zammadNr);
</script>
