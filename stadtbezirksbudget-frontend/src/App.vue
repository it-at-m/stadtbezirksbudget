<template>
  <v-app>
    <the-snackbar />
    <v-app-bar color="primary">
      <v-row align="center">
        <v-col
          class="d-flex align-center justify-start"
          cols="3"
        >
          <v-app-bar-nav-icon @click.stop="toggleDrawer()" />
          <router-link to="/">
            <v-toolbar-title class="font-weight-bold">
              <span class="text-white">Stadt</span>
              <span class="text-secondary">bezirks</span>
              <span class="text-white">budget</span>
            </v-toolbar-title>
          </router-link>
        </v-col>
        <v-col
          class="d-flex align-center justify-center"
          cols="6"
        >
          <v-text-field
            id="searchField"
            v-model="query"
            :prepend-inner-icon="mdiMagnify"
            clearable
            flat
            hide-details
            label="Suche"
            theme="dark"
            variant="solo-inverted"
            @keyup.enter="search"
          />
        </v-col>
        <v-col
          class="d-flex align-center justify-end"
          cols="3"
        >
          <antrag-list-filter />
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
import { mdiApps, mdiMagnify } from "@mdi/js";
import { AppSwitcher } from "@muenchen/appswitcher-vue";
import { useToggle } from "@vueuse/core";
import { onMounted, ref } from "vue";

import { getUser } from "@/api/user-client";
import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import TheSnackbar from "@/components/TheSnackbar.vue";
import { APPSWITCHER_URL } from "@/constants";
import { useSnackbarStore } from "@/stores/snackbar";
import { useUserStore } from "@/stores/user";
import User, { UserLocalDevelopment } from "@/types/User";
import AntragListFilter from "@/components/AntragListFilter.vue";

const query = ref<string>("");
const appswitcherBaseUrl = APPSWITCHER_URL;

const snackbarStore = useSnackbarStore();
const userStore = useUserStore();
const [drawer, toggleDrawer] = useToggle();

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

/**
 * Navigates to the page with the search results and sends an event to trigger further searches.
 */

async function search(): Promise<void> {
  if (query.value !== "" && query.value !== null) {
    snackbarStore.showMessage({
      message: "Sie haben nach " + query.value + " gesucht. ;)",
    });
  }
}
</script>
