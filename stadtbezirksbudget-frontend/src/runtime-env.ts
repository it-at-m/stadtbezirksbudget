declare global {
  interface Window {
    __RUNTIME_ENV__?: {
      ZAMMAD_BASE_URL: string;
      EAKTE_BASE_URL: string;
    };
  }
}

export {};
