import AUTHORITY_ENUM from "@/authority/authorityEnum";

/**
 * 判断当前登录用户是否具有某个权限
 * @param loginUser 当前登录用户
 * @param needAuthority 需要有的权限
 * @return boolean 有无权限
 */
const checkAuthority = (
  loginUser: any,
  needAuthority = AUTHORITY_ENUM.NOT_LOGIN
) => {
  const loginUserAuthority = loginUser?.userRole ?? AUTHORITY_ENUM.NOT_LOGIN;
  if (needAuthority === AUTHORITY_ENUM.NOT_LOGIN) return true;

  // 若权限等于用户，则只要用户登录就行了
  if (needAuthority === AUTHORITY_ENUM.USER) {
    // 若用户未登录
    if (loginUserAuthority === AUTHORITY_ENUM.NOT_LOGIN) return false;
  }

  // 若当前用户不是管理员
  if (needAuthority === AUTHORITY_ENUM.ADMIN) {
    if (loginUserAuthority !== AUTHORITY_ENUM.ADMIN) return false;
  }
  return true;
};

export default checkAuthority;
