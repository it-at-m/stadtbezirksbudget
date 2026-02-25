<template>
  <v-snackbar
    v-model="show"
    :timeout="-1"
  >
    Aktuell werden nicht alle Datensätze angezeigt, da Filter aktiviert sind.
    <v-btn
      color="primary"
      data-test="filter-notice-close"
      variant="text"
      @click="show = false"
    >
      Schließen
    </v-btn>
  </v-snackbar>
</template>

<script lang="ts" setup>
import { ref, watch } from "vue";

const props = defineProps<{
  active: boolean;
}>();
const show = ref<boolean>(props.active);
watch(
  () => props.active,
  (active) => (show.value = active)
);
</script>
