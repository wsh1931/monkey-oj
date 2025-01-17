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
import ManageQuestionView from "@/views/question/ManageQuestionView.vue";
import QuestionListView from "@/views/question/QuestionListView.vue";
import QuestionDetailView from "@/views/question/QuestionDetailView.vue";
import QuestionSubmitView from "@/views/question/QuestionSubmitView.vue";

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
    path: "/",
    name: "主页",
    component: ExampleView,
  },
  {
    path: "/question/list",
    name: "题目列表",
    component: QuestionListView,
  },

  {
    path: "/question/add",
    name: "创建题目",
    component: AddQuestionView,
    meta: {
      authority: AUTHORITY_ENUM.USER,
    },
  },
  {
    path: "/question/detail/:questionId",
    name: "题目详情",
    props: true,
    component: QuestionDetailView,
    meta: {
      access: AUTHORITY_ENUM.USER,
      hideInMenu: true,
    },
  },
  {
    path: "/question/submit",
    name: "提交题目列表",
    component: QuestionSubmitView,
  },
  {
    path: "/question/manage",
    name: "管理题目",
    component: ManageQuestionView,
    meta: {
      authority: AUTHORITY_ENUM.ADMIN,
    },
  },
  {
    path: "/question/update",
    name: "更新题目",
    component: AddQuestionView,
    meta: {
      authority: AUTHORITY_ENUM.ADMIN,
      hideInMenu: true,
    },
  },
  // {
  //   path: "/hide",
  //   name: "隐藏业面",
  //   component: HomeView,
  //   meta: {
  //     hideInMenu: true,
  //   },
  // },
  {
    path: "/notAuth",
    name: "没有权限",
    component: NotAuthView,
    meta: {
      hideInMenu: true,
    },
  },
  // {
  //   path: "/admin",
  //   name: "管理员可见",
  //   component: AdminView,
  //   meta: {
  //     authority: AUTHORITY_ENUM.ADMIN,
  //   },
  // },
  // {
  //   path: "/about",
  //   name: "about",
  //   component: () =>
  //     import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  // },
];
