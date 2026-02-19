<template>
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
  >
    <template #activator="{ props }">
      <v-row
        align="center"
        no-gutters
      >
        <v-col
          :style="{
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            whiteSpace: 'nowrap',
          }"
        >
          <eakte-link
            :aktenzeichen="aktenzeichen"
            :eakte-coo-adresse="eakteCooAdresse"
          />
        </v-col>
        <v-col cols="auto">
          <v-btn
            :icon="mdiDotsVertical"
            density="compact"
            v-bind="props"
            variant="flat"
            @click.stop
          />
        </v-col>
      </v-row>
    </template>
    <v-list>
      <eakte-coo-adresse-edit
        :antrag-id="antragId"
        :eakte-coo-adresse="eakteCooAdresse"
        @save="menu = false"
      />
    </v-list>
  </v-menu>
</template>

<script lang="ts" setup>
import { mdiDotsVertical } from "@mdi/js";
import { ref } from "vue";

import EakteCooAdresseEdit from "@/components/references/EakteCooAdresseEdit.vue";
import EakteLink from "@/components/references/EakteLink.vue";

const { antragId, aktenzeichen, eakteCooAdresse } = defineProps<{
  antragId: string;
  aktenzeichen: string;
  eakteCooAdresse: string;
}>();
const menu = ref<boolean>(false);
</script>
