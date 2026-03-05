<template>
  <status-select
    v-model:menu="menu"
    :item-props="itemProps"
  />
</template>

<script lang="ts" setup>
import type { StatusOption } from "@/types/antrag/Status.ts";

import { ref } from "vue";

import StatusSelect from "@/components/StatusSelect.vue";

const menu = ref(false);

const itemProps = (item: StatusOption) => ({
  subtitle: item.longText,
  title: menu.value ? item.shortText : `${item.shortText} - ${item.longText}`,
});
</script>
