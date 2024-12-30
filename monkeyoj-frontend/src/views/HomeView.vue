<template>
  <div class="home">
    <MdEditor :value="value" :handle-change="onChange"></MdEditor>
    <img alt="Vue logo" src="../assets/logo.jpg" />
    <HelloWorld msg="Welcome to Your Vue.js + TypeScript App" />
  </div>
</template>

<script lang="ts" setup>
import { defineComponent, ref } from "vue";
import HelloWorld from "@/components/HelloWorld.vue";
import MdEditor from "@/components/MdEditor.vue"; // @ is an alias to /src
const value = ref("");
const onChange = (v: string) => {
  value.value = v;
};
</script>

<style></style>
