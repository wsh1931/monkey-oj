<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>
<style></style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";
import { onMounted } from "vue";

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
