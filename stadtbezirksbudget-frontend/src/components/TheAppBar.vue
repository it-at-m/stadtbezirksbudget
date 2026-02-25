<template>
  <v-app-bar color="primary">
    <v-row align="center">
      <v-col
        class="d-flex align-center justify-start"
        cols="3"
      >
        <router-link
          class="text-decoration-none pa-4"
          to="/"
        >
          <v-toolbar-title class="font-weight-bold">
            <span class="text-white">Stadt</span>
            <span class="text-primary-variant">bezirks</span>
            <span class="text-white">budget</span>
          </v-toolbar-title>
        </router-link>
      </v-col>
      <v-col
        class="d-flex align-center justify-center"
        cols="6"
      >
        <antrag-list-search v-if="isHomePage" />
      </v-col>
      <v-col
        class="d-flex align-center justify-end"
        cols="3"
      >
        <antrag-list-sort-menu v-if="isHomePage" />
        <antrag-list-filter-menu v-if="isHomePage" />
        <app-switcher
          v-if="APPSWITCHER_URL"
          :base-url="APPSWITCHER_URL"
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
</template>

<script lang="ts" setup>
import { mdiApps } from "@mdi/js";
import { AppSwitcher } from "@muenchen/appswitcher-vue";

import AntragListFilterMenu from "@/components/antragList/AntragListFilterMenu.vue";
import AntragListSearch from "@/components/antragList/AntragListSearch.vue";
import AntragListSortMenu from "@/components/antragList/AntragListSortMenu.vue";
import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";
import { useRouteCheck } from "@/composables/useRouteCheck.ts";
import { APPSWITCHER_URL } from "@/constants.ts";
import { useUserStore } from "@/stores/useUserStore.ts";

const userStore = useUserStore();
const { isHomePage } = useRouteCheck();
</script>
