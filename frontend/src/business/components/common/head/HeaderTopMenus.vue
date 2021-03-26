<template>
  <el-menu mode="horizontal" menu-trigger="click"
           background-color="#004a71"
           class="header-top-menus"
           text-color="#F2F2F2"
           active-text-color="#FFF"
           :default-active="activeIndex"
           @select="handleSelect"
           router>

    <el-menu-item index="/dashboard" v-permission="['admin']">
      {{ $t('dashboard.dashboard') }}
    </el-menu-item>
    <el-menu-item index="/rule" v-permission="['admin']">
      {{ $t('rule.rule') }}
    </el-menu-item>
    <el-menu-item index="/resource" onselectstart="return false" v-permission="['admin']">
      {{ $t('resource.resource_result') }}
    </el-menu-item>
    <el-menu-item index="/account" onselectstart="return false" v-permission="['admin']">
      {{ $t('account.cloud_account') }}
    </el-menu-item>
    <el-menu-item index="/setting" onselectstart="return false">
      {{ $t('commons.system_setting') }}
    </el-menu-item>
  </el-menu>
</template>

<script>
  export default {
    name: "TopMenus",
    data() {
      return {
        activeIndex: "/"
      }
    },
    watch: {
      '$route'(to) {
        if (to.matched.length > 0) {
          this.activeIndex = to.matched[0].path;
        }
        this.handleSelect(this.activeIndex);
      }
    },
    mounted() {
      if (this.$route.matched.length > 0) {
        this.activeIndex = this.$route.matched[0].path;
      }
    },
    methods: {
      handleSelect(index) {
        this.activeIndex = index
      }
    }
  }
</script>
<style scoped>

  .el-menu >>> .el-menu-item {
    box-sizing: border-box;
    height: 37px;
  }

</style>
