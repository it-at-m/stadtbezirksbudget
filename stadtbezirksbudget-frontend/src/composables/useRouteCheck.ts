import { computed } from "vue";
import { useRoute } from "vue-router";

import { ROUTES_DETAILS, ROUTES_HOME } from "@/constants.ts";

/**
 * Composable that checks the current route and provides properties to determine if the current route is a specific page.
 */
export function useRouteCheck() {
  const route = useRoute();

  // Indicates whether the current route is the home page.
  const isHomePage = computed(() => route.name == ROUTES_HOME);
  // Indicates whether the current route is the details page.
  const isDetailsPage = computed(() => route.name == ROUTES_DETAILS);

  return {
    isHomePage,
    isDetailsPage,
  };
}
