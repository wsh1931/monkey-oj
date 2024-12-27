import HomeView from "../views/HomeView.vue";
import { RouteRecordRaw } from "vue-router";
import AdminView from "@/views/AdminView.vue";
import NotAuthView from "@/views/NotAuthView.vue";
import AUTHORITY_ENUM from "@/authority/authorityEnum";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "问题",
    component: HomeView,
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
