<template>
  <div id="globalHeader">
    <a-menu
      mode="horizontal"
      :selected-keys="selectedKeys"
      @menu-item-click="menuClick"
    >
      <a-menu-item key="0">
        <div class="logo-title">
          <img class="logo" src="../assets/logo.jpg" alt="花果山OJ" />
          <div class="title">猴&nbsp;OJ</div>
        </div>
      </a-menu-item>
      <a-menu-item v-for="route in routes" :key="route.path">
        {{ route.name }}
      </a-menu-item>
    </a-menu>
  </div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { ref } from "vue";

const router = useRouter();
const route = useRoute();

// 路由跳转后
router.afterEach((to, from, failure) => {
  selectedKeys.value = [to.path];
});

const selectedKeys = ref([route.path]);

const menuClick = (key: string) => {
  router.push({
    path: key,
  });
};
</script>

<style scoped>
.title {
  margin-left: 16px;
}

.logo-title {
  display: flex;
  align-items: center;
}

.logo {
  height: 48px;
  cursor: pointer;
}

#globalHeader {
}
</style>
