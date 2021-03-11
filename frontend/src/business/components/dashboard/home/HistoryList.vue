<template>
  <el-row :gutter="24" class="el-row-se">
    <el-col :span="24">
      <el-card class="box-card" shadow="always">
        <el-table border :data="tableData" class="adjust-table table-content" @sort-change="sort" :row-class-name="tableRowClassName"
                  @filter-change="filter" @select-all="select" @select="select">
          <el-table-column type="index" min-width="5%"/>
          <el-table-column :label="$t('history.cloud_account')" min-width="15%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <el-link type="primary" @click="viewRule(scope.row)">
                <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                {{scope.row.AccountName}}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column :label="$t('history.scan_score')" min-width="20%" show-overflow-tooltip>
            <template v-slot:default="scope">
              {{scope.row.scanScore}}
            </template>
          </el-table-column>
          <el-table-column :label="$t('history.resource_result')" min-width="30%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <span> {{scope.row.returnSum?scope.row.returnSum:0}}/{{scope.row.resourcesSum?scope.row.resourcesSum:0}}</span>
              <span> &nbsp;&nbsp;<i :class="scope.row.assets" ></i></span>
            </template>
          </el-table-column>
          <el-table-column min-width="30%" :label="$t('history.create_time')" sortable
                           prop="createTime">
            <template v-slot:default="scope">
              <span><i class="el-icon-time"></i> {{ scope.row.createTime | timestampFormatDayDate }}</span>
            </template>
          </el-table-column>
        </el-table>
        <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>

        <!--View rule-->
        <el-drawer class="rtl" :title="$t('rule.rule_detail')" :visible.sync="ruleVisible" size="70%" :before-close="handleClose" :direction="direction"
                   :destroy-on-close="true">
          <el-form :model="ruleForm" label-position="right" label-width="120px" size="small" ref="ruleForm">
            <el-form-item :label="$t('rule.rule_name')">
              <el-input v-model="ruleForm.name" autocomplete="off" :placeholder="$t('rule.rule_name')"/>
            </el-form-item>
            <el-form-item :label="$t('rule.rule_description')">
              <el-input v-model="ruleForm.description" autocomplete="off" :placeholder="$t('rule.rule_description')"/>
            </el-form-item>
            <el-form-item :label="$t('account.cloud_platform')">
              <el-select style="width: 100%;" v-model="ruleForm.pluginId" :placeholder="$t('account.please_choose_plugin')">
                <el-option
                  v-for="item in plugins"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                  <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                  &nbsp;&nbsp; {{$t(item.name)}}
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('rule.rule_tag')">
              <el-select style="width: 100%;" multiple v-model="ruleForm.tags" :placeholder="$t('rule.please_choose_tag')">
                <el-option
                  v-for="item in tags"
                  :key="item.tagKey"
                  :label="item.tagName"
                  :value="item.tagKey">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('rule.severity')">
              <el-select style="width: 100%;" v-model="ruleForm.severity" :placeholder="$t('rule.please_choose_severity')">
                <el-option
                  v-for="item in severityOptions"
                  :key="item.value"
                  :label="item.key"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('rule.rule_set')">
              <el-select style="width: 100%;" multiple filterable v-model="ruleForm.ruleSets">
                <el-option
                  v-for="item in ruleSetOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('rule.inspection_report')">
              <el-select style="width: 100%;" multiple filterable collapse-tags v-model="ruleForm.inspectionSeports">
                <el-option
                  v-for="item in inspectionSeportOptions"
                  :key="item.id"
                  :label="item.id + '. ' + item.project.substring(0, 50) + '...'"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('rule.rule_yml')">
              <codemirror ref="cmEditor" v-model="ruleForm.script" class="code-mirror" :options="cmOptions" />
            </el-form-item>
            <el-form-item>
              <el-table
                :data="ruleForm.parameter"
                style="width: 100%">
                <el-table-column v-slot:default="scope" label="Key" min-width="20%">
                  <el-input v-model="scope.row.key" :placeholder="$t('commons.input_content')"></el-input>
                </el-table-column>
                <el-table-column v-slot:default="scope" :label="$t('rule.middleware_name')" min-width="30%">
                  <el-input v-model="scope.row.name" :placeholder="$t('commons.input_content')"></el-input>
                </el-table-column>
                <el-table-column v-slot:default="scope" :label="$t('rule.middleware_parameter_default')" min-width="30%">
                  <el-input v-model="scope.row.defaultValue" :placeholder="$t('commons.input_content')"></el-input>
                </el-table-column>
                <el-table-column v-slot:default="scope" :label="$t('rule.required')" min-width="20%">
                  <el-switch v-model="scope.row.required" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-form>
          <dialog-footer
            @cancel="ruleVisible = false"/>
        </el-drawer>
        <!--View rule-->

      </el-card>
    </el-col>
  </el-row>
</template>

<script>

  import {_filter, _sort} from "@/common/js/utils";
  import TablePagination from "../../common/pagination/TablePagination";
  import DialogFooter from "../../common/components/DialogFooter";
  /* eslint-disable */
  const assets = [
    {key: "ec2", value: "el-icon-s-platform"},
    {key: "ecs", value: "el-icon-s-platform"},
    {key: "cvm", value: "el-icon-s-platform"},
    {key: "ami", value: "el-icon-picture"},
    {key: "s3", value: "el-icon-folder-opened"},
    {key: "oss", value: "el-icon-folder-opened"},
    {key: "obs", value: "el-icon-folder-opened"},
    {key: "cos", value: "el-icon-folder-opened"},
    {key: "security-group", value: "el-icon-lock"},
    {key: "network-addr", value: "el-icon-connection"},
    {key: "etc", value: "el-icon-s-platform"},
    {key: "asg", value: "el-icon-s-operation"},
    {key: "elb", value: "el-icon-s-operation"},
    {key: "slb", value: "el-icon-s-operation"},
    {key: "lambda", value: "el-icon-data-board"},
    {key: "autoscaling", value: "el-icon-s-operation"},
    {key: "account", value: "el-icon-s-custom"},
    {key: "eip", value: "el-icon-s-grid"},
    {key: "ip", value: "el-icon-s-grid"},
    {key: "ebs", value: "el-icon-wallet"},
    {key: "iam", value: "el-icon-s-tools"},
    {key: "rds", value: "el-icon-s-finance"},
    {key: "tag", value: "el-icon-s-flag"},
    {key: "vpc", value: "el-icon-menu"},
    {key: "vm", value: "el-icon-s-platform"},
    {key: "networksecuritygroup", value: "el-icon-lock"},
    {key: "disk", value: "el-icon-wallet"},
    {key: "storage", value: "el-icon-wallet"},
    {key: "others", value: "el-icon-s-unfold"},
  ];
  export default {
    name: "HistoryList",
    components: {
      TablePagination,
      DialogFooter,
    },
    props: {
      selectNodeIds: Array,
    },
    watch: {
      selectNodeIds() {
        this.search();
      },
      batchReportId() {
      }
    },
    data() {
      return {
        tags: [],
        tableData: [],
        currentPage: 1,
        pageSize: 10,
        total: 0,
        loading: false,
        condition: {
          components: Object
        },
        direction: 'rtl',
        ruleVisible: false,
        ruleForm: {},
        plugins: [],
        severityOptions: [],
        ruleSetOptions: [],
        inspectionSeportOptions: [],
        cmOptions: {
          tabSize: 4,
          mode: {
            name: 'shell',
            json: true
          },
          theme: 'bespin',
          lineNumbers: true,
          line: true,
          indentWithTabs: true,
        },
      }
    },
    created() {
    },
    watch: {
      selectNodeIds() {
        this.search();
      },
      batchReportId() {
      }
    },
    computed: {
    },
    methods: {
      init() {
        this.activePlugin();
        this.severityOptionsFnc();
        this.ruleSetOptionsFnc();
        this.inspectionSeportOptionsFnc();
        this.search();
      },
      //查询插件
      activePlugin() {
        let url = "/plugin/all";
        this.result = this.$get(url, response => {
          let data = response.data;
          this.plugins =  data;
        });
      },
      async viewRule (item) {
        await this.$get("/rule/get/" + item.id, response => {
          this.ruleForm = response.data;
          if (typeof(this.ruleForm.parameter) == 'string') this.ruleForm.parameter = JSON.parse(this.ruleForm.parameter);
          this.ruleForm.tagKey = this.ruleForm.tags[0];
          this.ruleVisible = true;
        });
      },
      //查询列表
      async search() {
        let url = "/dashboard/history/" + this.currentPage + "/" + this.pageSize;
        if (!!this.selectNodeIds) {
          this.condition.accountId = this.selectNodeIds[0];
        } else {
          this.condition.accountId = null;
        }
        this.result = await this.$post(url, this.condition, response => {
          let data = response.data;
          this.total = data.itemCount;
          this.tableData = data.listObject;
          for (let data of this.tableData) {
            this.getAssets(data);
          }
        });
      },
      sort(column) {
        _sort(column, this.condition);
        this.init();
      },
      filter(filters) {
        _filter(filters, this.condition);
        this.init();
      },
      handleClose() {
        this.ruleVisible =  false;
      },
      tableRowClassName({row, rowIndex}) {
        if (rowIndex%4 === 0) {
          return 'success-row';
        } else if (rowIndex%2 === 0) {
          return 'warning-row';
        } else {
          return '';
        }
      },
      async getAssets (item) {
        await this.$get("rule/getResourceTypesById/" + item.id, response => {
          item.assets = this.checkoutAssets(response.data);
        });
      },
      checkoutAssets (resource) {
        for(let item of assets){
          if (resource.indexOf(item.key) > -1) {
            return item.value;
          }
        }
        return "cloud_done";
      },
      severityOptionsFnc () {
        this.severityOptions = [
          {key: '低风险', value: "LowRisk"},
          {key: '中风险', value: "MediumRisk"},
          {key: '高风险', value: "HighRisk"}
        ];
      },
      ruleSetOptionsFnc () {
        this.$get("/rule/all/ruleGroups", res => {
          this.ruleSetOptions = res.data;
        });
      },
      inspectionSeportOptionsFnc () {
        this.$get("/rule/all/ruleInspectionReport", res => {
          this.inspectionSeportOptions = res.data;
        });
      },
      select(selection) {
        this.selection = selection.map(s => s.id);
        this.$emit('selection', selection);
      },
      isSelect(row) {
        return this.selection.includes(row.id)
      },
      edit(row) {
        this.$emit('edit', row);
      },
    },
    mounted() {
      this.init();
    },
  }
</script>

<style scoped>
  /deep/ .el-drawer__header {
    margin-bottom: 0px;
  }
  /deep/ :focus{outline:0;}
</style>
