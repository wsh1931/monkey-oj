<template>
  <div id="question-list">
    <a-form :model="searchParams" layout="inline">
      <a-form-item field="title" label="题目名称">
        <a-input
          style="min-width: 280px"
          v-model="searchParams.title"
          placeholder="请输入想查找的题目名称"
        />
      </a-form-item>
      <a-form-item field="tags" label="标签">
        <a-input-tag
          style="min-width: 280px"
          v-model="searchParams.tags"
          placeholder="请输入想查找的题目标签"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="doSubmit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider size="0" />
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
      @page-change="onPageChange"
      @page-size-change="onPageSizeChange"
    >
      <template #tags="{ record }">
        <a-space wrap>
          <a-tag
            v-for="(tag, index) of record.tags"
            :key="index"
            color="blue"
            bordered
            >{{ tag }}
          </a-tag>
        </a-space>
      </template>
      <template #acceptedRate="{ record }">
        {{
          `${record.submitNum ? record.acceptedNum / record.submitNum : "0"}%(${
            record.acceptedNum
          }/${record.submitNum})`
        }}
      </template>
      <template #createTime="{ record }">
        {{ moment(record.createTime).format("YYYY-MM-DD") }}
      </template>
      <template #optional="{ record }">
        <a-space>
          <a-button
            type="primary"
            status="success"
            @click="toQuestionDetail(record)"
            >查看
          </a-button>
        </a-space>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import moment from "moment";
import { onMounted, reactive, ref, watchEffect } from "vue";
import {
  Question,
  QuestionControllerService,
  QuestionQueryRequest,
  QuestionVO,
} from "../../../generated";
import message from "@arco-design/web-vue/es/message";
import { useRouter } from "vue-router";

const show = ref(true);
const router = useRouter();

const dataList = ref([]);
const searchParams = ref<QuestionQueryRequest>({
  title: "",
  tags: [],
  pageSize: 10,
  current: 1,
});
const total = ref(0);

const doSubmit = () => {
  searchParams.value = {
    ...searchParams.value,
    current: 1,
  };
};

onMounted(() => {
  loadData();
});

const onPageSizeChange = (pageSize: number) => {
  searchParams.value = {
    ...searchParams.value,
    pageSize: pageSize,
  };
};

// 得到题目列表
const loadData = async () => {
  const res = await QuestionControllerService.listQuestionVoByPageUsingPost(
    searchParams.value
  );
  if (res.code === 0) {
    dataList.value = res.data.records;
    total.value = res.data.total;
  } else {
    message.error("获取题目列表失败: " + res.message);
  }
};

const onPageChange = (page: number) => {
  searchParams.value = {
    ...searchParams.value,
    current: page,
  };
};

// 当loadData里面的数据发生变化时，就触发此行数
watchEffect(() => {
  loadData();
});
const columns = [
  {
    title: "题号",
    dataIndex: "id",
  },
  {
    title: "题目名称",
    dataIndex: "title",
  },
  {
    title: "标签",
    slotName: "tags",
  },
  {
    title: "通过率",
    slotName: "acceptedRate",
  },
  {
    title: "创建时间",
    slotName: "createTime",
  },
  {
    title: "操作",
    slotName: "optional",
  },
];

const toQuestionDetail = (questionVO: QuestionVO) => {
  router.push({
    path: `/question/detail/${questionVO.id}`,
  });
};
</script>

<style scoped>
#question-list {
  max-width: 1280px;
  margin: 0 auto;
}
</style>
