<template>
  <v-autocomplete
    :filterKeys="['raw.shortText', 'raw.longText']"
    :items="statusOptions"
    itemTitle="shortText"
    itemValue="value"
  >
    <template #item="{ props, item }">
      <v-list-item
        :subtitle="item.raw.longText"
        :title="item.raw.shortText"
        v-bind="props"
      />
    </template>
  </v-autocomplete>
</template>

<script lang="ts" setup>
import { statusOptions } from "@/types/Status.ts";
</script>

<style scoped>
/*noinspection CssUnusedSymbol*/
:deep(.v-field__input) {
  flex-wrap: nowrap;
}
/*noinspection CssUnusedSymbol*/
:deep(.v-autocomplete__selection) {
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
