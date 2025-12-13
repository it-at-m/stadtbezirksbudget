<template>
  <v-avatar>
    <img
      :alt="altText"
      :src="avatarUrl"
    />
  </v-avatar>
</template>
<script lang="ts" setup>
import { computed } from "vue";

import { DefaultLhmAvatarService } from "@/api/ad2image-avatar-client";

const {
  username,
  avatarMode = "fallbackGeneric",
  avatarSize = "64",
} = defineProps<{
  username: string;
  avatarMode?: string;
  avatarSize?: string;
}>();

const avatarUrl = computed(() => {
  return DefaultLhmAvatarService.avatarHref(username, avatarMode, avatarSize);
});

const altText = computed(() => `Bild von ${username}`);
</script>
