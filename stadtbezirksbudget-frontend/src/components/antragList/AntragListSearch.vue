<template>
  <v-text-field
    v-model="query"
    :prepend-inner-icon="mdiMagnify"
    clearable
    flat
    hide-details
    label="Suche"
    theme="dark"
    variant="solo-inverted"
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
