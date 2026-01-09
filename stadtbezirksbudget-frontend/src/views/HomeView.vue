<template>
  <v-container fluid>
    <v-row>
      <v-col
        class="text-left"
        cols="12"
      >
        <v-card rounded="lg"><antrag-list /></v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import AntragList from "@/components/antragList/AntragList.vue";
</script>
