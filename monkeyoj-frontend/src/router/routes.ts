import HomeView from "../views/HomeView.vue";
import { RouteRecordRaw } from "vue-router";
import AdminView from "@/views/AdminView.vue";
import NotAuthView from "@/views/NotAuthView.vue";

export const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "问题",
    component: HomeView,
    meta: {
      access: "canAdmin",
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
      access: "canAdmin",
    },
  },
  {
    path: "/about",
    name: "about",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
];
