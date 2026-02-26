<template>
  <v-snackbar
    v-model="show"
    :timeout="-1"
  >
    <v-row align="center">
      <v-col>
        Aktuell werden nicht alle Datensätze angezeigt, da Filter aktiviert
        sind.
      </v-col>
      <v-col cols="auto">
        <v-btn-primary
          data-test="filter-notice-close"
          variant="flat"
          @click="show = false"
        >
          Schließen
        </v-btn-primary>
      </v-col>
    </v-row>
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
