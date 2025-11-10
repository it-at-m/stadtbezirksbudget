<template>
  <Layout>
    <template #nav-bar-content-after>
      <div class="logo">
        <a
          href="https://www.muenchen.de/"
          target="_blank"
        >
          <img
            alt="Logo Landeshauptstadt München"
            src="https://assets.muenchen.de/logos/lhm/logo-lhm-muenchen.svg"
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
import { onMounted, onUnmounted, ref } from "vue";

const { Layout } = DefaultTheme;
const router = useRouter();
const zoom = ref(null);

const setupMediumZoom = () => {
  detachMediumZoom();
  zoom.value = mediumZoom("[data-zoomable]", {
    background: "transparent",
  });
};
const detachMediumZoom = () => {
  if (zoom.value) {
    zoom.value.detach();
  }
};

onMounted(setupMediumZoom);
onUnmounted(detachMediumZoom);
router.onAfterRouteChange = setupMediumZoom;
</script>

<style scoped>
.logo {
  display: flex;
  align-items: center;
}
@media (min-width: 768px) {
  .extra + .logo:before {
    width: 1px;
    height: 24px;
    margin-right: 16px;
    margin-left: 16px;
    content: "";
    background-color: var(--vp-c-divider);
  }
}
.logo img {
  height: 28px;
  filter: var(--muc-logo-filter);
}
</style>
