<template>
  <div id="app">
    <template v-if="route.path.startsWith('/user')">
      <router-view />
    </template>
    <template v-else>
      <BasicLayout />
    </template>
  </div>
</template>
<style scoped></style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";
import { useRoute } from "vue-router";
import UserLayout from "@/layouts/UserLayout.vue";

const route = useRoute();

/**
 * 全部初始化函数（全局单次调用的代码）
 */
const doInit = () => {
  console.log("hh, 你好");
};

onMounted(() => {
  doInit();
});
</script>
