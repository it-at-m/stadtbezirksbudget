<template>
  {{ antragsdatenSubsetList }}
  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="serverItems"
    :items-length="totalItems"
    :loading="loading"
    :search="search"
    item-value="name"
    @update:options="loadItems"
  ></v-data-table-server>
</template>

<script setup lang="ts">
import type AntragsdatenSubset from "@/types/AntragsdatenSubset.ts";
import type Page from "@/types/Page.ts";
import type { DataTableHeader } from "vuetify";

import { onMounted, ref } from "vue";

import { getAntragsdatenSubsetList } from "@/api/fetch-antragsdatenSubset-list.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";

const snackbarStore = useSnackbarStore();
const antragsdatenSubsetList = ref<Page<AntragsdatenSubset>>();

onMounted(() => {
  getAntragsdatenSubsetList(1, 15).catch((error) => {
    snackbarStore.showMessage(error);
  });
});

const itemsPerPage = ref(5);
const headers = ref<DataTableHeader[]>([
  { title: "Status", key: "antragsstatus", align: "start" },
  { title: "Nummer", key: "dummyTicketnummer", align: "start" },
  { title: "BA", key: "bezirksausschussnummer", align: "start" },
  { title: "Antragsdatum", key: "eingangsdatum", align: "start" },
  { title: "Projekt", key: "projekttitel", align: "start" },
  { title: "Antragsteller/in", key: "antragstellerName", align: "start" },
  { title: "Beantragtes Budget", key: "beantragtesBudget", align: "start" },
  { title: "Aktualisierung", key: "dummyAktualisierungsArt", align: "start" },
  {
    title: "Datum Aktualisierung",
    key: "dummyAktualisierungsDatum",
    align: "start",
  },
  { title: "Anmerkungen", key: "anmerkungen", align: "start" },
  { title: "Bearbeiter/in", key: "bearbeiter", align: "start" },
]);
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
      dummyTicketnummer: "Warten auf Zammad...",
      bezirksausschussnummer: item.bezirksausschussnummer,
      eingangsdatum: item.eingangsdatum,
      projekttitel: item.projekttitel,
      antragstellerName: item.antragstellerName,
      beantragtesBudget: item.beantragtesBudget,
      dummyAktualisierungsArt: "Warten auf Zammad...",
      dummyAktualisierungsDatum: "Warten auf Zammad...",
      anmerkungen: item.anmerkungen,
      bearbeiter: item.bearbeiter,
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
