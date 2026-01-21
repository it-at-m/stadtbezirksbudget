<template>
  <v-text-field
    v-model="query"
    :prepend-inner-icon="mdiMagnify"
    clearable
    density="comfortable"
    hide-details
    label="Suche"
    variant="underlined"
    @focusout="search"
    @keyup.enter="search"
    @click:clear="search"
  />
</template>

<script lang="ts" setup>
import { mdiMagnify } from "@mdi/js";

import { useAntragListSearch } from "@/composables/useAntragListSearch.ts";

const { query, search } = useAntragListSearch();
</script>
