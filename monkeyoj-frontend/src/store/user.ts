import { StoreOptions } from "vuex";

export default {
  namespaced: true,
  // 全局参数
  state: () => ({
    loginUser: {
      userName: "未登录",
    },
  }),
  // 定义更新变量的方法
  mutations: {
    updateUser(state, playload) {
      state.loginUser = playload;
    },
  },
  // 执行异步，调用mutations的方法
  actions: {
    getLoginUser({ commit, state }, playload) {
      // todo 从后端获取登录信息
      commit("updateUser", playload);
    },
  },
} as StoreOptions<any>;
