<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>
<style></style>
<script setup lang="ts">
import BasicLayout from "@/layouts/BasicLayout.vue";

import { useRouter } from "vue-router";
import { useStore } from "vuex";

const router = useRouter();
const store = useStore();

// router.beforeEach((to, from, next) => {});
</script>
