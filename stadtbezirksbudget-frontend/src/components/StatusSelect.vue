<template>
  <v-autocomplete
    v-model="model"
    :filterKeys="['raw.shortText', 'raw.longText']"
    :item-props="itemProps"
    :items="statusOptions"
    itemTitle="shortText"
    itemValue="value"
    v-bind="$attrs"
  />
</template>

<script lang="ts" setup>
import type { StatusOption } from "@/types/Status.ts";

import { statusOptions } from "@/types/Status.ts";

const model = defineModel();

const itemProps = (item: StatusOption) => ({
  subtitle: item.longText,
  title: item.shortText,
});
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
