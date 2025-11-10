<template>
  <Layout>
    <template #nav-bar-content-after>
      <div class="logo">
        <a
          href="https://www.muenchen.de/"
          target="_blank"
        >
          <img
            src="https://assets.muenchen.de/logos/lhm/logo-lhm-muenchen.svg"
            alt="Logo Landeshauptstadt München"
          />
        </a>
      </div>
    </template>
  </Layout>
</template>

<script setup>
import mediumZoom from "medium-zoom";
import { useRouter } from "vitepress";
import DefaultTheme from "vitepress/theme";
import { onMounted } from "vue";

const { Layout } = DefaultTheme;
const router = useRouter();
const setupMediumZoom = () => {
  mediumZoom("[data-zoomable]", {
    background: "transparent",
  });
};
onMounted(setupMediumZoom);
router.onAfterRouteChange = setupMediumZoom;
</script>

<style scoped>
.logo {
  display: flex;
  align-items: center;
}
@media (min-width: 768px) {
  .extra + .logo:before {
    margin-right: 16px;
    margin-left: 16px;
    width: 1px;
    height: 24px;
    background-color: var(--vp-c-divider);
    content: "";
  }
}
.logo img {
  filter: var(--muc-logo-filter);
  height: 28px;
}
</style>
