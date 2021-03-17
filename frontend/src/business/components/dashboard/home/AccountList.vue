<template>
  <el-card class="table-card" v-loading="result.loading">
    <template v-slot:header>
      <span class="title">{{$t('account.cloud_account')}}</span>
    </template>
    <account-pie-chart/>
  </el-card>
</template>

<script>

  import AccountPieChart from "@/business/components/common/chart/AccountPieChart";

  export default {
    name: "AccountList",
    components: {
      AccountPieChart,
    },
    data() {
      return {
        result: {},
      }
    },

    methods: {
    },

    created() {
    },
  }
</script>

<style scoped>
.table-card {
  margin-bottom: 2%;
  min-height: 26%;
}
</style>
