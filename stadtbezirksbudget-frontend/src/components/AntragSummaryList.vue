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
    data-test="antrag-summary-list"
    disable-sort
    @update:options="updateOptions"
  >
    <template v-slot:[`header.beantragtesBudget`]>
      <div
        class="text-left"
        data-test="header-beantragtes-budget"
      >
        Beantragtes<br />Budget [€]
      </div>
    </template>
    <template v-slot:[`item.status`]="{ item }">
      <antrag-status-select
        :antrag-id="item.id"
        :status="item.status"
      />
    </template>
    <template v-slot:[`item.eingangDatum`]="{ item }">
      <span data-test="item-eingang-datum">{{
        toDateString(new Date(item.eingangDatum))
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
    <template v-slot:[`item.istFehlbetrag`]="{ item }">
      <span data-test="item-ist-fehlbetrag">{{
        booleanToFestOrFehl(item.istFehlbetrag)
      }}</span>
    </template>
  </v-data-table-server>
</template>

<script lang="ts" setup>
import type { DataTableHeader } from "vuetify";

import { useDebounceFn } from "@vueuse/core";
import { computed, onMounted, onUnmounted, ref } from "vue";

import AntragStatusSelect from "@/components/common/AntragStatusSelect.vue";
import { useAntragSummaryList } from "@/composables/useAntragSummaryList.ts";
import { StatusText } from "@/types/Status.ts";
import {
  booleanToFestOrFehl,
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
