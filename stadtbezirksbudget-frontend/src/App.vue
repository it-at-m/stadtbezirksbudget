<template>
  <v-app>
    <the-snackbar />
    <the-app-bar />
    <v-main>
      <v-container fluid>
        <router-view v-slot="{ Component }">
          <v-fade-transition mode="out-in">
            <component :is="Component" />
          </v-fade-transition>
        </router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts" setup>
import TheAppBar from "@/components/TheAppBar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { useInitializeStores } from "@/composables/useInitializeStores.ts";

useInitializeStores();
</script>
