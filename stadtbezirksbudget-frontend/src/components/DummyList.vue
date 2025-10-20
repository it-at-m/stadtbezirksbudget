<template>
  {{ dummyList }}
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
import type Dummy from "@/types/Dummy.ts";
import type Page from "@/types/Page.ts";
import type { DataTableHeader } from "vuetify";

import { onMounted, ref } from "vue";

import { getDummyList } from "@/api/fetch-dummy-list.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";

const snackbarStore = useSnackbarStore();
const dummyList = ref<Page<Dummy>>();

onMounted(() => {
  getDummyList(1, 15)
    .then((content: Page<Dummy>) => (dummyList.value = content))
    .catch((error) => {
      snackbarStore.showMessage(error);
    });
});

const itemsPerPage = ref(5);
const headers = ref<DataTableHeader[]>([
  { title: "Name", key: "name", align: "start" },
  { title: "Email", key: "email", align: "start" },
  { title: "Alter", key: "age", align: "end" },
  { title: "Übergewichtig?", key: "isOverweight", align: "center" },
]);
const search = ref("");
const serverItems = ref<Dummy[]>([]);
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
    const pageResponse: Page<Dummy> = await getDummyList(
      page - 1,
      itemsPerPage
    );

    serverItems.value = pageResponse.content;
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
