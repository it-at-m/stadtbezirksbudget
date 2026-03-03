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

<script generic="T = Status | Status[]" lang="ts" setup>
import type { Status, StatusOption } from "@/types/Status.ts";

import { statusOptions } from "@/types/Status.ts";

const model = defineModel<T>();

const itemProps = (item: StatusOption) => ({
  subtitle: item.longText,
  title: item.shortText,
});
</script>
