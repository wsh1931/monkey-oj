<template>
  <div>
    <a-row id="globalHeader" class="grid-demo" align="center" :wrap="false">
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
            <a-menu-item v-for="route in visibleRoutes" :key="route.path">
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
import { computed, ref } from "vue";
import { useStore } from "vuex";
import checkAuthority from "@/authority/checkAuthority";
import AUTHORITY_ENUM from "@/authority/authorityEnum";

const router = useRouter();
const route = useRoute();
const store = useStore();

// 使用computed是因为当用户信息发生变更时出发页面重新渲染
const visibleRoutes = computed(() => {
  return routes.filter((route) => {
    // 判断是否为隐藏菜单
    if (route.meta?.hideInMenu) {
      return false;
    }
    // 判断当前用户是否有权限展现菜单。
    if (
      !checkAuthority(
        store.state.user.loginUser,
        route?.meta?.authority as string
      )
    ) {
      return false;
    }
    return true;
  });
});

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

setTimeout(() => {
  store.dispatch("user/getLoginUser", {
    userName: "已登录",
    userRole: AUTHORITY_ENUM.ADMIN,
  });
}, 1000);
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
