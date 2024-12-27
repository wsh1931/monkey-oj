import { StoreOptions } from "vuex";
import AUTHORITY_ENUM from "@/authority/authorityEnum";

export default {
  namespaced: true,
  // 全局参数
  state: () => ({
    loginUser: {
      userName: "未登录",
      userRole: AUTHORITY_ENUM.NOT_LOGIN,
    },
  }),
  // 定义更新变量的方法
  mutations: {
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
  // 执行异步，调用mutations的方法
  actions: {
    getLoginUser({ commit, state }, payload) {
      // todo 从后端获取登录信息
      commit("updateUser", payload);
    },
  },
} as StoreOptions<any>;
