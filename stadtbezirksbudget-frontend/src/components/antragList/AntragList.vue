<template>
  <v-data-table-server
    v-model:sortBy="sortBy"
    :cell-props="{
      style: {
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
      },
    }"
    :headers="computedHeaders"
    :hover="true"
    :items="items"
    :items-length="totalItems"
    :items-per-page="itemsPerPage"
    :loading="loading"
    :page="page"
    data-test="antrag-list"
    items-per-page-text="Anträge pro Seite:"
    @update:options="updateOptions"
    @click:row="goToDetails"
  >
    <template v-slot:[`item.status`]="{ item }">
      <antrag-status-update
        :antrag-id="item.id"
        :initial-status="item.status"
        data-test="item-status"
        @status-updated="fetchItems"
        @click.stop
        @mousedown.stop
      />
    </template>
    <template v-slot:[`item.eingangDatum`]="{ item }">
      <span data-test="item-eingang-datum">{{
        toDateString(new Date(item.eingangDatum))
      }}</span>
    </template>
    <template v-slot:[`item.aktualisierung`]="{ item }">
      <span data-test="item-aktualisierung-art">{{
        AktualisierungArtText[item.aktualisierung]
      }}</span>
    </template>
    <template v-slot:[`item.aktualisierungDatum`]="{ item }">
      <span data-test="item-aktualisierung-datum">{{
        toDateString(new Date(item.aktualisierungDatum))
      }}</span>
    </template>
    <template v-slot:[`item.beantragtesBudget`]="{ item }">
      <span data-test="item-beantragtes-budget">{{
        toNumberString(item.beantragtesBudget, 0)
      }}</span>
    </template>
    <template v-slot:[`item.finanzierungArt`]="{ item }">
      <span data-test="item-finanzierung-art">{{
        FinanzierungArtText[item.finanzierungArt]
      }}</span>
    </template>
    <template v-slot:[`item.zammadNr`]="{ item }">
      <zammad-link
        :zammad-nr="item.zammadNr"
        data-test="item-zammad-nr"
        @click.stop
        @mousedown.stop
      />
    </template>
    <template v-slot:[`item.aktenzeichen`]="{ item }">
      <eakte-reference
        :aktenzeichen="item.aktenzeichen"
        :antrag-id="item.id"
        :eakte-coo-adresse="item.eakteCooAdresse"
        data-test="item-aktenzeichen"
        @reference-updated="fetchItems"
        @click.stop
        @mousedown.stop
      />
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import type { DataTableHeader } from "vuetify";

import { useDebounceFn } from "@vueuse/core";
import { computed, onMounted, onUnmounted, ref } from "vue";

import AntragStatusUpdate from "@/components/AntragStatusUpdate.vue";
import EakteReference from "@/components/references/EakteReference.vue";
import ZammadLink from "@/components/references/ZammadLink.vue";
import { useAntragList } from "@/composables/useAntragList.ts";
import { AktualisierungArtText } from "@/types/AktualisierungArt.ts";
import { FinanzierungArtText } from "@/types/FinanzierungArt.ts";
import { toDateString, toNumberString } from "@/util/formatter.ts";

const {
  items,
  totalItems,
  page,
  itemsPerPage,
  loading,
  sortBy,
  fetchItems,
  updateOptions,
  goToDetails,
} = useAntragList();

const screenWidth = ref(window.innerWidth);

const updateScreenWidth = useDebounceFn(() => {
  screenWidth.value = window.innerWidth;
}, 150);

onMounted(() => {
  window.addEventListener("resize", updateScreenWidth);
});

onUnmounted(() => {
  window.removeEventListener("resize", updateScreenWidth);
});

const computedHeaders = computed<DataTableHeader[]>(() => {
  const baseWidth = (screenWidth.value * 0.95) / 11;
  return [
    {
      title: "Status",
      key: "status",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Nummer",
      key: "zammadNr",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Aktenzeichen",
      key: "aktenzeichen",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "BA",
      key: "bezirksausschussNr",
      align: "end",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Antragsdatum",
      key: "eingangDatum",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Antragsteller*in",
      key: "antragstellerName",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Projekt",
      key: "projektTitel",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Budget [€]",
      key: "beantragtesBudget",
      align: "end",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Art",
      key: "finanzierungArt",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Aktualisierung",
      key: "aktualisierung",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Datum Aktualisierung",
      key: "aktualisierungDatum",
      maxWidth: `${baseWidth}px`,
    },
  ];
});
</script>
