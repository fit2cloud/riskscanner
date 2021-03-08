// const requireContext = require.context('@/business/components/xpack/', true, /router\.js$/)

export default {
  path: "/setting",
  name: "Setting",
  components: {
    content: () => import(/* webpackChunkName: "setting" */ '@/business/components/settings/Setting')
  },
  children: [
    {
      path: 'user',
      component: () => import(/* webpackChunkName: "setting" */ '@/business/components/settings/system/User'),
      meta: {system: true, title: 'commons.user'}
    },
    {
      path: 'systemparametersetting',
      component: () => import(/* webpackChunkName: "setting" */ '@/business/components/settings/system/SystemParameterSetting'),
      meta: {system: true, title: 'commons.system_parameter_setting'}
    },
    {
      path: 'messagesetting',
      component: () => import('@/business/components/settings/system/MessageSetting'),
      meta: {system: true, title: 'system_parameter_setting.message.setting'}
    },
    {
      path: 'personsetting',
      component: () => import(/* webpackChunkName: "setting" */ '@/business/components/settings/personal/PersonSetting'),
      meta: {person: true, title: 'commons.personal_setting'}
    },
    {
      path: 'apikeys',
      component: () => import(/* webpackChunkName: "setting" */ '@/business/components/settings/personal/ApiKeys'),
      meta: {
        person: true,
        title: 'commons.api_keys',
        roles: ['admin']
      }
    },
  ]
}
