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
import AntragSummaryList from "@/components/AntragSummaryList.vue";
</script>
