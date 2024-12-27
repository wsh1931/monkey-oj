<template>
  <div>
    <a-row
      id="globalHeader"
      class="grid-demo"
      style="margin-bottom: 16px"
      align="center"
    >
      <a-col flex="100px">
        <div>100px</div>
      </a-col>
      <a-col flex="auto">
        <div>
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
      </a-col>
      <a-col flex="100px">
        <div>{{ store.state.user?.loginUser?.userName ?? "未登录" }}</div>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { routes } from "@/router/routes";
import { useRoute, useRouter } from "vue-router";
import { ref } from "vue";
import { useStore } from "vuex";

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

const store = useStore();

// setTimeout(() => {
//   store.dispatch("user/getLoginUser", {
//     userName: "已登录",
//     role: "admin",
//   });
// }, 10000);
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
