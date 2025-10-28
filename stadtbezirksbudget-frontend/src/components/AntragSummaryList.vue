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
      {{ toDateTimeString(new Date(item.aktualisierungDatum)) }}
    </template>
    <template v-slot:[`item.beantragtesBudget`]="{ item }">
      {{ toNumberString(item.beantragtesBudget, 0) }}
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import type { DataTableHeader } from "vuetify";

import { computed, ref } from "vue";

import { useAntragSummaryList } from "@/composables/antragSummaryList.ts";
import { StatusText } from "@/types/Status.ts";
import {
  toDateString,
  toDateTimeString,
  toNumberString,
} from "@/util/formatter.ts";

const { items, totalItems, page, itemsPerPage, loading, updateOptions } =
  useAntragSummaryList();

const screenWidth = ref(window.innerWidth);
const computedHeaders = computed<DataTableHeader[]>(() => {
  const baseWidth = (screenWidth.value * 0.95) / 11;
  const percentage = (screenWidth.value * 0.95) / 100;
  return [
    {
      title: "Status",
      key: "status",
      maxWidth: `${baseWidth + percentage}px`,
    },
    {
      title: "Nummer",
      key: "zammadNr",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "BA",
      key: "bezirksausschussNr",
      align: "end",
      maxWidth: `${baseWidth - 5 * percentage}px`,
    },
    {
      title: "Antragsdatum",
      key: "eingangDatum",
      maxWidth: `${baseWidth - 2 * percentage}px`,
    },
    {
      title: "Projekt",
      key: "projektTitel",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Antragsteller/in",
      key: "antragstellerName",
      maxWidth: `${baseWidth + 2 * percentage}px`,
    },
    {
      key: "beantragtesBudget",
      align: "end",
      maxWidth: `${baseWidth - 2 * percentage}px`,
    },
    {
      title: "Aktualisierung",
      key: "aktualisierung",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Datum Aktualisierung",
      key: "aktualisierungDatum",
      maxWidth: `${baseWidth - 2 * percentage}px`,
    },
    {
      title: "Anmerkungen",
      key: "anmerkungen",
      maxWidth: `${baseWidth + 8 * percentage}px`,
      class: `truncate`,
    },
    {
      title: "Bearbeiter/in",
      key: "bearbeiter",
      maxWidth: `${baseWidth}px`,
    },
  ];
});
</script>
