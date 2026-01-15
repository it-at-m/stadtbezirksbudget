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
    <v-row>
      <v-col><p>Just for testing, needs to be removed:</p></v-col>
    </v-row>
    <v-row>
      <v-col>
        <v-btn color="primary">Primary</v-btn>
        <v-btn color="secondary">Secondary</v-btn>
        <v-btn color="tertiary">Tertiary</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import AntragList from "@/components/antragList/AntragList.vue";
</script>
