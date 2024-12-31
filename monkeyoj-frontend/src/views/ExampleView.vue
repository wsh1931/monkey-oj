<template>
  <div class="home">
    <MdEditor :value="value" :handle-change="onChange"></MdEditor>
    <CodeEditor :value="codeValue" :handle-change="onCodeChange"></CodeEditor>
  </div>
</template>

<script lang="ts" setup>
import { defineComponent, ref } from "vue";
import MdEditor from "@/components/MdEditor.vue";
import CodeEditor from "@/components/CodeEditor.vue"; // @ is an alias to /src
const mdValue = ref("");
const onMdChange = (v: string) => {
  mdValue.value = v;
  console.log(v);
};
const codeValue = ref("");
const onCodeChange = (v: string) => {
  codeValue.value = v;
  console.log("onCodeChange: = " + v);
};
</script>

<style></style>
