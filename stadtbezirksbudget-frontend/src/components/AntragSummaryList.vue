<template>
  <v-data-table-server
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
    disable-sort
    @update:options="updateOptions"
  >
    <template v-slot:[`header.beantragtesBudget`]>
      <div class="text-left">Beantragtes<br />Budget [€]</div>
    </template>
    <template v-slot:[`item.status`]="{ item }">
      {{ StatusText[item.status] }}
    </template>
    <template v-slot:[`item.eingangDatum`]="{ item }">
      {{ toDateString(new Date(item.eingangDatum)) }}
    </template>
    <template v-slot:[`item.aktualisierungDatum`]="{ item }">
      {{ toDateString(new Date(item.aktualisierungDatum)) }}
    </template>
    <template v-slot:[`item.beantragtesBudget`]="{ item }">
      {{ toNumberString(item.beantragtesBudget, 0) }}
    </template>
    <template v-slot:[`item.istFehlbetrag`]="{ item }">
      {{ booleanToString(item.istFehlbetrag) }}
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import type { DataTableHeader } from "vuetify";

import { useDebounceFn } from "@vueuse/core";
import { computed, onMounted, onUnmounted, ref } from "vue";

import { useAntragSummaryList } from "@/composables/antragSummaryList.ts";
import { StatusText } from "@/types/Status.ts";
import {
  booleanToString,
  toDateString,
  toNumberString,
} from "@/util/formatter.ts";

const { items, totalItems, page, itemsPerPage, loading, updateOptions } =
  useAntragSummaryList();

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
      title: "Antragsteller/in",
      key: "antragstellerName",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Projekt",
      key: "projektTitel",
      maxWidth: `${baseWidth}px`,
    },
    {
      key: "beantragtesBudget",
      align: "end",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Art",
      key: "istFehlbetrag",
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
