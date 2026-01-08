<template>
  <v-card>
    <v-card-title class="mb-3">
      <v-row align="center">
        <v-col>
          <h2>{{ title }}</h2>
        </v-col>
        <v-col cols="auto">
          <v-btn
            :icon="mdiPencil"
            color="#006A63"
            density="comfortable"
            variant="elevated"
          />
        </v-col>
      </v-row>
    </v-card-title>
    <v-card-text>
      <slot />
    </v-card-text>
  </v-card>
</template>

<script lang="ts" setup>
import { mdiPencil } from "@mdi/js";

const { title } = defineProps<{ title: string }>();
</script>
