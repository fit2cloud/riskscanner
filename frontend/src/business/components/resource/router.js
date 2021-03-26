/* eslint-disable */
export default {
  path: "/resource",
  name: "resource",
  redirect: "/resource/result",
  components: {
    content: () => import(/* webpackChunkName: "setting" */ "@/business/components/resource/base")
  },
  children: [
    {
      path: "statistics",
      name: "statistics",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/resource/home/Statistics"),
    },
    {
      path: "result",
      name: "result",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/resource/home/Result"),
    },
    {
      path: "resultdetails/:id",
      name: "resultdetails",
      component: () => import(/* webpackChunkName: "api" */ "@/business/components/resource/home/ResultDetails"),
    },
  ]
}
