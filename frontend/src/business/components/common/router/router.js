import Vue from "vue";
import VueRouter from 'vue-router';
import RouterSidebar from "./RouterSidebar";
import axios from "axios";
import Setting from "@/business/components/settings/router";
import Account from "@/business/components/account/router";
import Rule from "@/business/components/rule/router";
import Resource from "@/business/components/resource/router";
import Dashboard from "@/business/components/dashboard/router";

Vue.use(VueRouter);
/* eslint-disable */
const router = new VueRouter({
  routes: [
    {path: "/", redirect: '/setting/personsetting'},
    {
      path: "/sidebar",
      components: {
        sidebar: RouterSidebar
      }
    },
    Setting,
    Account,
    Rule,
    Resource,
    Dashboard
  ]
});

router.beforeEach((to, from, next) => {
  //解决localStorage清空，cookie没失效导致的卡死问题
  if (!localStorage.getItem('Admin-Token')) {
    axios.get("/signout");
    localStorage.setItem('Admin-Token', "{}");
    window.location.href = "/login";
    next();
  } else {
    next();
  }
});

export default router
