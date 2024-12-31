<template>
  <!-- 定义一个div作为Monaco编辑器的容器，并使用ref="codeEditorRef"来引用它 -->
  <div id="code-editor" ref="codeEditorRef" style="min-height: 400px"></div>
  <!-- 注释掉的按钮，原本用于触发fillValue函数填充编辑器内容 -->
  <a-button @click="fillValue">测试</a-button>
</template>

<script setup lang="ts">
// 导入Monaco编辑器库
import * as monaco from "monaco-editor";
// 从vue包中导入必要的函数和类型
import { defineProps, onMounted, ref, toRaw, withDefaults } from "vue";

// 定义组件接收的props类型
interface Props {
  value: string; // 编辑器初始内容
  handleChange: (v: string) => void; // 当编辑器内容变化时调用的回调函数
}

// 使用withDefaults为props提供默认值
const props = withDefaults(defineProps<Props>(), {
  value: () => "", // 默认初始内容为空字符串
  handleChange: (v: string) => {
    console.log(v); // 默认回调函数，仅打印内容到控制台
  },
});

// 创建一个ref来引用DOM元素（编辑器容器）
const codeEditorRef = ref();
// 创建一个ref来存储Monaco编辑器实例
const codeEditor = ref();

// 定义一个函数，用于填充编辑器内容（当前被注释掉未使用）
const fillValue = () => {
  if (!codeEditor.value) return; // 如果编辑器实例不存在，则直接返回
  // 使用toRaw来获取编辑器实例的原始值（实际上这里不需要toRaw，因为codeEditor.value已经是原始值）
  // 但由于Vue 3的响应式系统，通常不需要对Monaco编辑器实例使用toRaw
  toRaw(codeEditor.value).setValue("新的值"); // 设置编辑器内容为"新的值"
};

// 使用onMounted生命周期钩子，在组件挂载后初始化编辑器
onMounted(() => {
  if (!codeEditorRef.value) {
    return; // 如果编辑器容器不存在，则直接返回
  }
  // 使用Monaco编辑器的create方法创建编辑器实例，并传入配置选项
  codeEditor.value = monaco.editor.create(codeEditorRef.value, {
    value: props.value, // 设置编辑器初始内容
    language: "java", // 设置编辑器语言为Java
    automaticLayout: true, // 启用自动布局调整
    minimap: {
      enabled: true, // 启用缩略图视图
    },
    // 其他可选配置...
    readOnly: false, // 设置编辑器为可编辑状态
    theme: "vs-dark", // 设置编辑器主题为暗色主题
  });

  // 监听编辑器内容变化事件，并调用props中的handleChange回调
  codeEditor.value.onDidChangeModelContent(() => {
    // 使用getValue方法获取当前编辑器内容，并调用handleChange回调
    // 注意：这里同样不需要toRaw，因为codeEditor.value已经是原始编辑器实例
    props.handleChange(toRaw(codeEditor.value!).getValue()); // 使用非空断言操作符!，因为我们知道在onMounted钩子中codeEditor.value已经被赋值
  });
});
</script>

<style scoped>
/* 样式部分，当前为空，可以根据需要添加样式 */
</style>
