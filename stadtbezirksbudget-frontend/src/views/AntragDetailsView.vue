<template>
  <v-container>
    <h1>Detailansicht</h1>
    <p>Antrag ID: {{ route.params.id }}</p>
  </v-container>
</template>

<script lang="ts" setup>
import { useRoute } from "vue-router";

const route = useRoute();
</script>
