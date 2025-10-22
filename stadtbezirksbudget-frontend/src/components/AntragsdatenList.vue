<template>
  {{ antragsdatenSubsetList }}
  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :headers="computedHeaders"
    :header-props="{ style: { color: '#757575' } }"
    :items="serverItems"
    :items-length="totalItems"
    :loading="loading"
    :search="search"
    :hover="true"
    item-value="name"
    :cell-props="{
      style: {
        overflow: 'hidden',
        textOverflow: 'ellipsis',
        whiteSpace: 'nowrap',
      },
    }"
    @update:options="loadItems"
  ></v-data-table-server>
</template>

<script setup lang="ts">
import type AntragsdatenSubset from "@/types/AntragsdatenSubset.ts";
import type Page from "@/types/Page.ts";
import type { DataTableHeader } from "vuetify";

import { computed, onMounted, ref } from "vue";

import { getAntragsdatenSubsetList } from "@/api/fetch-antragsdatenSubset-list.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";

const snackbarStore = useSnackbarStore();
const antragsdatenSubsetList = ref<Page<AntragsdatenSubset>>();
const screenWidth = ref(window.innerWidth);

onMounted(() => {
  getAntragsdatenSubsetList(1, 15).catch((error) => {
    snackbarStore.showMessage(error);
  });
});

const itemsPerPage = ref(5);
const computedHeaders = computed<DataTableHeader[]>(() => {
  const baseWidth = screenWidth.value / 11;
  const percentage = screenWidth.value / 100;
  return [
    {
      title: "Status",
      key: "antragsstatus",
      align: "start",
      maxWidth: `${baseWidth + percentage}px`,
    },
    {
      title: "Nummer",
      key: "dummyTicketnummer",
      align: "start",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "BA",
      key: "bezirksausschussnummer",
      align: "end",
      maxWidth: `${baseWidth - 5 * percentage}px`,
    },
    {
      title: "Antragsdatum",
      key: "eingangsdatum",
      align: "start",
      maxWidth: `${baseWidth - 2 * percentage}px`,
    },
    {
      title: "Projekt",
      key: "projekttitel",
      align: "start",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Antragsteller/in",
      key: "antragstellerName",
      align: "start",
      maxWidth: `${baseWidth + 2 * percentage}px`,
    },
    {
      title: "Beantragtes Budget",
      key: "beantragtesBudget",
      align: "end",
      maxWidth: `${baseWidth - 3 * percentage}px`,
    },
    {
      title: "Aktualisierung",
      key: "dummyAktualisierungsArt",
      align: "start",
      maxWidth: `${baseWidth}px`,
    },
    {
      title: "Datum Aktualisierung",
      key: "dummyAktualisierungsDatum",
      align: "start",
      maxWidth: `${baseWidth - 2 * percentage}px`,
    },
    {
      title: "Anmerkungen",
      key: "anmerkungen",
      align: "start",
      maxWidth: `${baseWidth + 9 * percentage}px`,
      class: `truncate`,
    },
    {
      title: "Bearbeiter/in",
      key: "bearbeiter",
      align: "start",
      maxWidth: `${baseWidth}px`,
    },
  ];
});
const search = ref("");
const serverItems = ref<AntragsdatenSubset[]>([]);
const loading = ref(false);
const totalItems = ref(0);
async function loadItems({
  page,
  itemsPerPage,
}: {
  page: number;
  itemsPerPage: number;
}) {
  loading.value = true;
  try {
    const pageResponse: Page<AntragsdatenSubset> =
      await getAntragsdatenSubsetList(page - 1, itemsPerPage);

    console.debug("Antwort von getAntragsdatenSubsetList:", pageResponse);

    serverItems.value = pageResponse.content.map((item) => ({
      antragsstatus: item.antragsstatus,
      dummyTicketnummer: "ZM-10011001",
      bezirksausschussnummer: item.bezirksausschussnummer,
      eingangsdatum: item.eingangsdatum,
      projekttitel: item.projekttitel,
      antragstellerName: item.antragstellerName,
      beantragtesBudget: item.beantragtesBudget,
      dummyAktualisierungsArt: "Fachanwendung",
      dummyAktualisierungsDatum: "01.01.2025",
      anmerkungen: item.anmerkungen,
      bearbeiter: item.bearbeiter,
      id: item.id,
    }));
    totalItems.value = pageResponse.page.totalElements;
  } catch (error) {
    let errorMessage = "Ein unbekannter Fehler ist aufgetreten.";
    if (error instanceof Error) {
      errorMessage = error.message;
    }
    snackbarStore.showMessage({
      message: errorMessage,
      level: STATUS_INDICATORS.WARNING,
      show: true,
    });
  } finally {
    loading.value = false;
  }
}
</script>
