<template>
  <div id="manage-question">
    <a-table
      :columns="columns"
      :data="dataList"
      @change="changePage"
      :pagination="{
        total: total,
        pageSize: searchParams.pageSize,
        current: searchParams.current,
        showTotal: true,
        showPageSize: true,
      }"
    >
      <template #optional="{ record }">
        <a-space>
          <a-button type="primary" @click="doUpdate(record)">修改</a-button>
          <a-button status="danger" @click="doDelete(record)">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import {Question, QuestionControllerService} from "../../../generated";
import message from "@arco-design/web-vue/es/message";

const show = ref(true);

const dataList = ref([]);
const searchParams = ref({
  pageSize: 10,
  current: 1,
});
const total = ref(0);

onMounted(() => {
  loadData();
});

// 得到题目列表
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("获取题目列表失败: " + res.message);
  }
};
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "标题",
    dataIndex: "title",
  },
  {
    title: "内容",
    dataIndex: "content",
  },
  {
    title: "标签",
    dataIndex: "tags",
  },
  {
    title: "答案",
    dataIndex: "answer",
  },
  {
    title: "提交数",
    dataIndex: "submitNum",
  },
  {
    title: "通过数",
    dataIndex: "acceptedNum",
  },
  {
    title: "判题配置",
    dataIndex: "judgeConfig",
  },
  {
    title: "判题用例",
    dataIndex: "judgeCase",
  },

  {
    title: "用户ID",
    dataIndex: "userId",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];

const doDelete = async (question: Question) => {
  const res = await QuestionControllerService.deleteQuestionUsingPost({
    id: question.id,
  });
  if (res.code === 0) {
    message.success("删除成功");
  } else {
    message.error("操作失败" + res.message);
  }
};
const doUpdate = (question: Question) => {
  console.log(question);
};
</script>
