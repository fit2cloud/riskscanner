<template>
  <div id="menu-bar" v-if="isRouterAlive">
    <el-row type="flex">
      <el-col :span="12">
        <el-menu class="header-menu" :unique-opened="true" mode="horizontal" router :default-active='$route.path'>
          <el-menu-item :index="path">
            {{ $t("account.cloud_account_setting") }}
          </el-menu-item>

          <el-menu-item :index="'/account/accountoverview'">
            {{ $t("account.statistical_analysis") }}
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="12"/>
    </el-row>
  </div>

</template>

<script>

export default {
  name: "HeaderMenus",
  components: {},
  data() {
    return {
      path: '/account/cloudaccount',
      isRouterAlive: true,
    }
  },
  methods: {
    reload() {
      this.isRouterAlive = false;
      this.$nextTick(function () {
        this.isRouterAlive = true;
      });
    },
    init() {
      let path = this.$route.path;
      if (path.indexOf("/account/cloudaccount") >= 0 || path.indexOf("/account/accountscan") >= 0) {
        this.path = path;
        this.reload();
      }
    },
  },
  watch: {
    '$route'(to) {
      this.init();
    }
  },
  mounted() {
    this.init();
  }
}

</script>

<style scoped>
#menu-bar {
  border-bottom: 1px solid #E6E6E6;
  background-color: #FFF;
}

</style>
