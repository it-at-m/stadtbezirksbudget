/// <reference types="vite/client" />

import { ZAMMAD_TICKET_PATH } from "@/constants.ts";

declare module "*.vue" {
  import type { DefineComponent } from "vue";
  const component: DefineComponent<{}, {}, any>;
  export default component;
}

interface ImportMetaEnv {
  readonly VITE_AD2IMAGE_URL: string;
  readonly VITE_APPSWITCHER_SERVER_URL: string;
  readonly VITE_ZAMMAD_BASE_URL: string;
  readonly VITE_ZAMMAD_TICKET_PATH: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
