<template>
  {{ dummyList }}
</template>

<script setup lang="ts">
import type Dummy from "@/types/Dummy.ts";
import type Page from "@/types/Page.ts";

import { onMounted, ref } from "vue";

import { getDummyList } from "@/api/fetch-dummy-list.ts";
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
</script>
