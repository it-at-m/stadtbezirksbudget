<template>
  <div>
    <v-row class="text-grey-darken-1">
      <v-col>{{ label }}</v-col>
    </v-row>
    <v-row no-gutters>
      <v-col>
        <component
          :is="inputComponent"
          v-model="fromModel"
          :label="fromLabel"
          data-test="from-input"
          v-bind="inputProps"
        />
      </v-col>
      <v-col>
        <component
          :is="inputComponent"
          v-model="toModel"
          :label="toLabel"
          data-test="to-input"
          v-bind="inputProps"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script lang="ts" setup>
const fromModel = defineModel("from");
const toModel = defineModel("to");

const {
  label,
  fromLabel = "von",
  toLabel = "bis",
  inputComponent,
  inputProps,
} = defineProps<{
  label: string;
  fromLabel?: string;
  toLabel?: string;
  inputComponent: string | object;
  inputProps?: Record<string, unknown>;
}>();
</script>
