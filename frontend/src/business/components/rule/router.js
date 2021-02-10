export default {
  path: "/rule",
  name: "rule",
  redirect: "/rule/rule",
  components: {
    content: () => import(/* webpackChunkName: "setting" */ '@/business/components/rule/base')
  },
  children: [
    {
      path: 'rule',
      name: 'rule',
      component: () => import(/* webpackChunkName: "api" */ '@/business/components/rule/home/Rule'),
    },
    {
      path: "ruletag",
      name: "ruletag",
      component: () => import(/* webpackChunkName: "api" */ '@/business/components/rule/home/RuleTag'),
    },
    {
      path: "rulegroup",
      name: "rulegroup",
      component: () => import(/* webpackChunkName: "api" */ '@/business/components/rule/home/RuleGroup'),
    },
  ]
}
