<template>
  <v-app>
    <the-snackbar />
    <the-app-bar @toggle-navigation-drawer="toggleNavigationDrawer" />
    <v-navigation-drawer v-model="navigationDrawer">
      <div class="pas-4 d-flex justify-center">
        <img
          :src="logo"
          alt="Logo"
          width="100%"
        />
      </div>
    </v-navigation-drawer>
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

import logo from "@/assets/logo.png";
import TheAppBar from "@/components/TheAppBar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { useInitializeStores } from "@/composables/useInitializeStores.ts";

useInitializeStores();

const [navigationDrawer, toggleNavigationDrawer] = useToggle();
</script>
