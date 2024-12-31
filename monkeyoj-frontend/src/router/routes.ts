import HomeView from "../views/ExampleView.vue";
import { RouteRecordRaw } from "vue-router";
import AdminView from "@/views/AdminView.vue";
import NotAuthView from "@/views/NotAuthView.vue";
import AUTHORITY_ENUM from "@/authority/authorityEnum";
import UserLayout from "@/layouts/UserLayout.vue";
import UserLoginView from "@/views/user/UserLoginView.vue";
import UserRegisterView from "@/views/user/UserRegisterView.vue";
import ExampleView from "@/views/ExampleView.vue";
import AddQuestionView from "@/views/question/AddQuestionView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/user",
    name: "用户",
    component: UserLayout,
    meta: {
      hideInMenu: true,
    },
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: UserLoginView,
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: UserRegisterView,
      },
    ],
  },
  {
    path: "/add/question",
    name: "创建题目",
    component: AddQuestionView,
    meta: {
      authority: AUTHORITY_ENUM.ADMIN,
    },
  },
  {
    path: "/",
    name: "测试",
    component: ExampleView,
  },
  {
    path: "/hide",
    name: "隐藏业面",
    component: HomeView,
    meta: {
      hideInMenu: true,
    },
  },
  {
    path: "/notAuth",
    name: "没有权限",
    component: NotAuthView,
  },
  {
    path: "/admin",
    name: "管理员可见",
    component: AdminView,
    meta: {
      authority: AUTHORITY_ENUM.ADMIN,
    },
  },
  {
    path: "/about",
    name: "about",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
];
