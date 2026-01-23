<template>
  <v-app>
    <the-snackbar />
    <the-app-bar @toggle-navigation-drawer="toggleNavigationDrawer" />
    <v-navigation-drawer v-model="navigationDrawer"></v-navigation-drawer>
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
import { useToggle } from "@vueuse/core";

import TheAppBar from "@/components/TheAppBar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { useInitializeStores } from "@/composables/useInitializeStores.ts";

useInitializeStores();

const [navigationDrawer, toggleNavigationDrawer] = useToggle();
</script>
