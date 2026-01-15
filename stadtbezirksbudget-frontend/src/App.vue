<template>
  <v-app>
    <the-snackbar />
    <v-app-bar color="surface-variant">
      <v-row align="center">
        <v-col
          class="d-flex align-center justify-start"
          cols="3"
        >
          <v-app-bar-nav-icon @click.stop="toggleDrawer()" />
          <router-link class="text-decoration-none" to="/">
            <v-toolbar-title class="font-weight-bold">
              <span class="text-white">Stadt</span>
              <span class="text-primary">bezirks</span>
              <span class="text-white">budget</span>
            </v-toolbar-title>
          </router-link>
        </v-col>
        <v-col
          class="d-flex align-center justify-center"
          cols="6"
        >
          <antrag-list-search />
        </v-col>
        <v-col
          class="d-flex align-center justify-end"
          cols="3"
        >
          <antrag-list-sort-menu />
          <antrag-list-filter-menu />
          <app-switcher
            v-if="appswitcherBaseUrl"
            :base-url="appswitcherBaseUrl"
            :icon="mdiApps"
            :tags="['global']"
          />
          <v-btn
            icon
            variant="text"
          >
            <ad2-image-avatar
              v-if="userStore.getUser !== null"
              :username="userStore.getUser.username"
            />
          </v-btn>
        </v-col>
      </v-row>
    </v-app-bar>
    <v-navigation-drawer v-model="drawer"></v-navigation-drawer>
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
import { mdiApps } from "@mdi/js";
import { AppSwitcher } from "@muenchen/appswitcher-vue";
import { useToggle } from "@vueuse/core";
import { onMounted } from "vue";

import { getUser } from "@/api/user-client";
import AntragListFilterMenu from "@/components/antragList/AntragListFilterMenu.vue";
import AntragListSearch from "@/components/antragList/AntragListSearch.vue";
import AntragListSortMenu from "@/components/antragList/AntragListSortMenu.vue";
import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { useInitializeStores } from "@/composables/useInitializeStores.ts";
import { APPSWITCHER_URL } from "@/constants";
import { useUserStore } from "@/stores/useUserStore.ts";
import User, { UserLocalDevelopment } from "@/types/User";

const appswitcherBaseUrl = APPSWITCHER_URL;

const userStore = useUserStore();
const [drawer, toggleDrawer] = useToggle();

useInitializeStores();

onMounted(() => {
  loadUser();
});

/**
 * Loads UserInfo from the backend and sets it in the store.
 */
function loadUser(): void {
  getUser()
    .then((user: User) => userStore.setUser(user))
    .catch(() => {
      // No user info received, so fallback
      if (import.meta.env.DEV) {
        userStore.setUser(UserLocalDevelopment());
      } else {
        userStore.setUser(null);
      }
    });
}
</script>
