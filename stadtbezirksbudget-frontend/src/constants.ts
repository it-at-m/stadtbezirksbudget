export const ROUTES_HOME = "home";
export const ROUTES_DETAILS = "details";

export const BACKEND = "/api/backend-service";
export const ZAMMAD_TICKET_PATH = "/#ticket/zoom/";
export const EAKTE_PATH = "/fsc/fscasp/content/bin/fscvext.dll?bx=";
export const ZAMMAD_BASE_URL =
  window.__RUNTIME_ENV__?.ZAMMAD_BASE_URL ?? "https://zammad.muenchen.de";
export const EAKTE_BASE_URL =
  window.__RUNTIME_ENV__?.EAKTE_BASE_URL ?? "https://akte.muenchen.de";
export const AD2IMAGE_URL = import.meta.env.VITE_AD2IMAGE_URL;
export const APPSWITCHER_URL = import.meta.env.VITE_APPSWITCHER_URL;

export const SNACKBAR_DEFAULT_TIMEOUT = 5000;

export const enum STATUS_INDICATORS {
  SUCCESS = "success",
  INFO = "info",
  WARNING = "warning",
  ERROR = "error",
}
