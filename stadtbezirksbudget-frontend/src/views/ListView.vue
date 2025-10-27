<template>
  <v-container fluid>
    <v-row>
      <v-col
        cols="12"
        class="text-left"
      >
        <v-card rounded="lg"><antrag-summary-list /></v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";

import { checkHealth } from "@/api/health-client";
import AntragSummaryList from "@/components/AntragSummaryList.vue";
import { useSnackbarStore } from "@/stores/snackbar";
import HealthState from "@/types/HealthState";

const snackbarStore = useSnackbarStore();
const status = ref("DOWN");

onMounted(() => {
  checkHealth()
    .then((content: HealthState) => (status.value = content.status))
    .catch((error) => {
      snackbarStore.showMessage(error);
    });
});
</script>

<style scoped>
.UP {
  color: limegreen;
}

.DOWN {
  color: lightcoral;
}
</style>
