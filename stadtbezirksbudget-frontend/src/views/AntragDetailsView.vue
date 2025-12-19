<template>
  <v-container>
    <h1>Detailansicht</h1>
    <p>Antrag ID: {{ route.params.id }}</p>
    <zammad-button zammad-nr="00000021" />
  </v-container>
</template>

<script lang="ts" setup>
import { useRoute } from "vue-router";

import ZammadButton from "@/components/ZammadButton.vue";

const route = useRoute();
</script>
