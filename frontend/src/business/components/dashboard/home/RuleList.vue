<template>
  <el-card class="table-card" v-loading="result.loading">
    <template v-slot:header>
      <span class="title">{{$t('rule.rule')}}</span>
    </template>
    <rule-list-pie-chart/>
  </el-card>
</template>

<script>
  import RuleListPieChart from "@/business/components/common/chart/RuleListPieChart";
  export default {
    name: "RuleList",
    components: {
      RuleListPieChart,
    },
    data() {
      return {
        result: {},
      }
    },

    methods: {
    },

    mounted() {
    },
  }
</script>

<style scoped>
.table-card {
  margin-bottom: 2%;
  min-height: 46%;
}
</style>
