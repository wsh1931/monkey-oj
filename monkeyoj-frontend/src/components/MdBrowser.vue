<template>
  <Viewer :value="value" :plugins="plugins" />
</template>

<script setup lang="ts">
import gfm from "@bytemd/plugin-gfm";
import highlight from "@bytemd/plugin-highlight";
import { ref, withDefaults, defineProps } from "vue";
import { Viewer } from "@bytemd/vue-next";

// 定义接口
interface Props {
  value: string;
}

// 定义value 和 handleChange的默认值
const props = withDefaults(defineProps<Props>(), {
  value: () => "",
});
const plugins = [
  gfm(),
  highlight(),
  // Add more plugins here
];
</script>

<style></style>
