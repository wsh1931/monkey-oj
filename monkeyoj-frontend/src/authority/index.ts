import router from "@/router";
import store from "@/store";
import AUTHORITY_ENUM from "@/authority/authorityEnum";
import checkAuthority from "@/authority/checkAuthority";

router.beforeEach(async (to, from, next) => {
  const loginUser = store.state.user.loginUser;
  // 没登录实现自动登录
  if (!loginUser || !loginUser.userRole) {
    // 待用户登录成功之后，再执行后面的代码
    await store.dispatch("user/getLoginUser");
  }

  const needAuthority =
    (to.meta?.authority as string) ?? AUTHORITY_ENUM.NOT_LOGIN;
  // 跳转的页面需要登录
  if (needAuthority !== AUTHORITY_ENUM.NOT_LOGIN) {
    // 没登陆则要跳转到登录页面
    if (!loginUser || !loginUser.userRole) {
      // 若用户登录成功则跳转到to.fullPath页面
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }
    // 若已经登录但是权限不足，则跳转到无权限页面
    if (!checkAuthority(loginUser, needAuthority)) {
      next("/notAuth");
      return;
    }
  }
  next();
});
