<template>
  <v-container fluid>
    <v-row>
      <v-col
        class="text-left"
        cols="12"
      >
        <v-card rounded="lg"><antrag-summary-list /></v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import AntragSummaryList from "@/components/AntragSummaryList.vue";
</script>
