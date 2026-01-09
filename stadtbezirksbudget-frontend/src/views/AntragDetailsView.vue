<template>
  <v-container v-if="details">
    <v-row
      v-for="(card, index) in cards"
      :key="index"
    >
      <v-col>
        <component
          :is="card"
          :antrag="details"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import type { Component } from "vue";

import { useRoute } from "vue-router";

import DetailsInformationenAntrag from "@/components/antragDetails/DetailsInformationenAntrag.vue";
import { useAntragDetails } from "@/composables/useAntragDetails.ts";
import { routeParamsToString } from "@/util/converter.ts";

const route = useRoute();
const { details } = useAntragDetails(routeParamsToString(route.params.id));

const cards: Component[] = [DetailsInformationenAntrag];
</script>
