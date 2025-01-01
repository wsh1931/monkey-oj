<template>
  <div class="userLogin">
    <h2>用户登录</h2>
    <a-form
      style="max-width: 480px; margin: 0 auto"
      label-align="left"
      auto-label-width
      :model="form"
      @submit="handleSubmit"
    >
      <a-form-item field="userAccount" label="账号">
        <a-input v-model="form.userAccount" placeholder="请输入用户账号" />
      </a-form-item>
      <a-form-item field="userPassword" label="密码" tooltip="密码不少于 8 位">
        <a-input-password
          v-model="form.userPassword"
          placeholder="请输入用户密码"
        />
      </a-form-item>
      <a-form-itemq>
        <a-button html-type="submit" style="width: 120px" type="primary"
          >登录
        </a-button>
      </a-form-itemq>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue";
import message from "@arco-design/web-vue/es/message";
import {
  UserControllerService,
  type UserLoginRequest,
} from "../../../generated";
import { useRouter } from "vue-router";
import store from "@/store";

const router = useRouter();

const form = reactive({
  userAccount: "",
  userPassword: "",
} as UserLoginRequest);
const handleSubmit = async () => {
  const res = await UserControllerService.userLoginUsingPost(form);
  if (res.code === 0) {
    await store.dispatch("user/getLoginUser");
    message.success("登录成功, 欢迎: " + JSON.stringify(res.data.userName));
    router.push({
      path: "/",
      // replace的作用是点击游览器后退键不会回到登录页面
      replace: true,
    });
  } else {
    message.error("登录失败: " + res.message);
  }
};
</script>
