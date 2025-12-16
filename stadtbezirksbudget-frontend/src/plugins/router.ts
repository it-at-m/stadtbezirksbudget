// Composables
import { createRouter, createWebHistory } from "vue-router";

import { ROUTES_DETAILS, ROUTES_HOME } from "@/constants";
import AntragDetailsView from "@/views/AntragDetailsView.vue";
import HomeView from "@/views/HomeView.vue";

const routes = [
  {
    path: "/",
    name: ROUTES_HOME,
    component: HomeView,
    meta: {},
  },
  {
    path: "/antrag/:id",
    name: ROUTES_DETAILS,
    component: AntragDetailsView,
    meta: {},
  },
  { path: "/:catchAll(.*)*", redirect: "/" }, // CatchAll route
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return {
      top: 0,
      left: 0,
    };
  },
});

export default router;
