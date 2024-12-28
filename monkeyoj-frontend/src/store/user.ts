import { StoreOptions } from "vuex";
import AUTHORITY_ENUM from "@/authority/authorityEnum";
import { UserControllerService } from "../../generated";

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
    updateUser(state, payload) {
      state.loginUser = payload;
    },
  },
  // 执行异步，调用mutations的方法
  actions: {
    async getLoginUser({ commit, state }, payload) {
      // 从后端获取登录信息
      const user = await UserControllerService.getLoginUserUsingGet();
      console.log(user);
      if (user.code === 0) {
        commit("updateUser", user.data);
        console.log(user);
      } else {
        commit("updateUser", {
          ...state.loginUser,
          userRole: AUTHORITY_ENUM.NOT_LOGIN,
        });
      }
    },
  },
} as StoreOptions<any>;
