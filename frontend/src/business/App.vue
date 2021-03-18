<template>
  <el-col v-if="auth">
    <el-row id="header-top" type="flex" justify="space-between" align="middle">

      <el-col :span="12">
        <a class="logo"/>
        <cs-top-menus/>
      </el-col>

      <el-col :span="12" class="align-right">
        <!-- float right -->
        <cs-user/>
        <cs-language-switch/>
        <help/>
      </el-col>
    </el-row>

    <cs-view/>
  </el-col>
</template>

<script>
  import CsTopMenus from "./components/common/head/HeaderTopMenus";
  import CsView from "./components/common/router/CsView";
  import CsUser from "./components/common/head/HeaderUser";
  import Help from "./components/common/head/Help";
  import CsLanguageSwitch from "./components/common/head/LanguageSwitch";
  import {saveLocalStorage} from "../common/js/utils";

  export default {
    name: 'app',
    data() {
      return {
        auth: false
      }
    },
    beforeCreate() {
      this.$get("/isLogin").then(response => {
        if (response.data.success) {
          this.$setLang(response.data.data.language);
          saveLocalStorage(response.data);
          this.auth = true;
        } else {
          window.location.href = "/login"
        }
      }).catch(() => {
        window.location.href = "/login"
      });
    },
    components: {CsLanguageSwitch, CsUser, CsView, CsTopMenus, Help},
    methods: {}
  }
</script>

<style scoped>
  #header-top {
    width: 100%;
    padding: 0 10px;
    background-color: #004a71;
    color: rgb(245, 245, 245);
    font-size: 14px;
  }

  .logo {
    width: 156px;
    margin-bottom: 0;
    border: 0;
    margin-right: 20px;
    display: inline-block;
    line-height: 37px;
    background-size: 156px 30px;
    box-sizing: border-box;
    height: 37px;
    background-repeat: no-repeat;
    background-position: 50% center;
    background-image: url("../assets/logo-red.png");
  }

  .menus > * {
    color: inherit;
    padding: 0;
    max-width: 180px;
    white-space: pre;
    cursor: pointer;
    line-height: 40px;
  }

  .header-top-menus {
    display: inline-block;
    border: 0;
  }

  .menus > a {
    padding-right: 15px;
    text-decoration: none;
  }

  .align-right {
    float: right;
  }
</style>

