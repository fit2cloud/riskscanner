<template>
    <main-container>
      <el-card class="table-card" v-loading="result.loading">
        <template v-slot:header>
          <table-header :condition.sync="condition" @search="search"
                           :title="$t('account.cloud_account_list')"
                           @create="create" :createTip="$t('account.create')"
                           @scan="scan" :scanTip="$t('account.one_scan')"
                           @validate="validate" :runTip="$t('account.one_validate')"
                           :show-run="true" :show-scan="true" :show-create="true"/>

        </template>

        <el-table border :data="tableData" class="adjust-table table-content" @sort-change="sort"
                  :row-class-name="tableRowClassName"
                  @filter-change="filter" @select-all="select" @select="select">
          <el-table-column type="selection" min-width="5%">
          </el-table-column>
          <el-table-column type="index" min-width="5%"/>
          <el-table-column prop="name" :label="$t('account.name')" min-width="12%" show-overflow-tooltip></el-table-column>
          <el-table-column :label="$t('account.cloud_platform')" min-width="10%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <span>
                <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                 &nbsp;&nbsp; {{scope.row.pluginName}}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="status" min-width="8%" :label="$t('account.status')"
                           column-key="status"
                           :filters="statusFilters"
                           :filter-method="filterStatus">
            <template v-slot:default="{row}">
              <account-status :row="row"/>
            </template>
          </el-table-column>
          <el-table-column min-width="15%" :label="$t('account.create_time')" sortable
                           prop="createTime">
            <template v-slot:default="scope">
              <span><i class="el-icon-time"></i> {{ scope.row.createTime | timestampFormatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column min-width="15%" :label="$t('account.update_time')" sortable
                           prop="updateTime">
            <template v-slot:default="scope">
              <span><i class="el-icon-time"></i> {{ scope.row.updateTime | timestampFormatDate }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="userName" :label="$t('account.creator')" min-width="8%" show-overflow-tooltip/>
          <el-table-column :label="$t('account.regions')" min-width="7%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <regions :row="scope.row.regions"/>
            </template>
          </el-table-column>
          <el-table-column min-width="15%" :label="$t('commons.operating')">
            <template v-slot:default="scope">
              <table-operators :buttons="buttons" :row="scope.row"/>
            </template>
          </el-table-column>
        </el-table>
        <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
      </el-card>

      <!--Create account-->
      <el-drawer class="rtl" :title="$t('account.create')" :visible.sync="createVisible" size="65%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="form" label-position="right" label-width="150px" size="small" :rules="rule" ref="accountForm">
          <el-form-item :label="$t('account.name')"  ref="name" prop="name">
            <el-input v-model="form.name" autocomplete="off" :placeholder="$t('account.input_name')"/>
          </el-form-item>
          <el-form-item :label="$t('account.cloud_platform')" :rules="{required: true, message: $t('account.cloud_platform'), trigger: 'change'}">
            <el-select style="width: 100%;" v-model="form.pluginId" :placeholder="$t('account.please_choose_plugin')" @change="changePlugin(form.pluginId)">
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
          <div v-for="(tmp, index) in tmpList" :key="index">
            <el-form-item v-if="tmp.inputType === 'password'" :label="tmp.label" style="margin-bottom: 29px">
              <el-input :type="tmp.inputType" v-model="tmp.input" autocomplete="new-password" show-password :placeholder="tmp.description"/>
            </el-form-item>
            <el-form-item v-if="tmp.inputType != 'password' && tmp.inputType != 'boolean'" :label="tmp.label">
              <el-input :type="tmp.inputType" v-model="tmp.input" autocomplete="off" :placeholder="tmp.description"/>
            </el-form-item>
          </div>
          <el-form-item v-if="script">
            <el-link type="danger" @click="innerDrawer = true">{{$t('account.iam_strategy')}}</el-link>
            <div>
              <el-drawer
                size="45%"
                :title="$t('account.iam_strategy')"
                :append-to-body="true"
                :before-close="innerDrawerClose"
                :visible.sync="innerDrawer">
                <el-form-item>
                  <codemirror ref="cmEditor" v-model="script" class="code-mirror" :options="cmOptions" />
                </el-form-item>
              </el-drawer>
            </div>
          </el-form-item>
        </el-form>
        <dialog-footer
          @cancel="createVisible = false"
          @confirm="saveAccount(form, 'add')"/>
      </el-drawer>
      <!--Create account-->

      <!--Update account-->
      <el-drawer class="rtl" :title="$t('account.update')" :visible.sync="updateVisible" size="65%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="form" label-position="right" label-width="150px" size="small" :rules="rule" ref="accountForm">
          <el-form-item :label="$t('account.name')"  ref="name" prop="name">
            <el-input v-model="form.name" autocomplete="off" :placeholder="$t('account.input_name')"/>
          </el-form-item>
          <el-form-item :label="$t('account.cloud_platform')" :rules="{required: true, message: $t('account.cloud_platform'), trigger: 'change'}">
            <el-select style="width: 100%;" disabled v-model="form.pluginId" :placeholder="$t('account.please_choose_plugin')" @change="changePlugin(form.pluginId)">
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
          <div v-for="(tmp, index) in tmpList" :key="index">
            <el-form-item v-if="tmp.inputType === 'password'" :label="tmp.label" style="margin-bottom: 29px">
              <el-input :type="tmp.inputType" v-model="tmp.input" @input="change($event)" autocomplete="new-password" show-password :placeholder="tmp.description"/>
            </el-form-item>
            <el-form-item v-if="tmp.inputType != 'password' && tmp.inputType != 'boolean'" :label="tmp.label">
              <el-input :type="tmp.inputType" v-model="tmp.input" @input="change($event)" autocomplete="off" :placeholder="tmp.description"/>
            </el-form-item>
          </div>
          <el-form-item v-if="script">
            <el-link type="danger" @click="innerDrawer = true">{{$t('account.iam_strategy')}}</el-link>
            <div>
              <el-drawer
                size="45%"
                :title="$t('account.iam_strategy')"
                :append-to-body="true"
                :before-close="innerDrawerClose"
                :visible.sync="innerDrawer">
                <el-form-item>
                  <codemirror ref="cmEditor" v-model="script" class="code-mirror" :options="cmOptions" />
                </el-form-item>
              </el-drawer>
            </div>
          </el-form-item>
        </el-form>
        <dialog-footer
          @cancel="updateVisible = false"
          @confirm="saveAccount(form, 'update')"/>
      </el-drawer>
      <!--Update account-->

      <!-- 一键扫描选择扫描组 -->
      <el-dialog :close-on-click-modal="false"
                 :title="$t('account.scan')"
                 :visible.sync="scanVisible"
                 class="" >

      </el-dialog>
      <!-- 一键扫描选择扫描组 -->

    </main-container>
</template>

<script>
  import TablePagination from "../../common/pagination/TablePagination";
  import TableHeader from "../head/TableHeader";
  import TableOperator from "../../common/components/TableOperator";
  import Container from "../../common/components/Container";
  import MainContainer from "../../common/components/MainContainer";
  import AccountStatus from "./AccountStatus";
  import Regions from "./Regions";
  import TableOperators from "../../common/components/TableOperators";
  import {_filter, _sort} from "@/common/js/utils";
  import {ACCOUNT_CONFIGS} from "../../common/components/search/search-components";
  import DialogFooter from "../../common/components/DialogFooter";
  import {ACCOUNT_NAME} from "../../../../common/js/constants";

  /* eslint-disable */
  export default {
    components: {
      TableOperators,
      AccountStatus,
      Regions,
      MainContainer,
      Container,
      TableHeader,
      TablePagination,
      TableOperator,
      DialogFooter
    },
    provide() {
      return {
        search: this.search,
      }
    },
    data() {
      return {
        credential: {},
        result: {},
        condition: {
          components: ACCOUNT_CONFIGS
        },
        tableData: [],
        currentPage: 1,
        pageSize: 10,
        total: 0,
        loading: false,
        selectIds: new Set(),
        createVisible: false,
        updateVisible: false,
        scanVisible: false,
        innerDrawer: false,
        plugins: [],
        tmpList: [],
        item: {},
        form: {},
        regions: "",
        script: '',
        direction: 'rtl',
        rule: {
          name: [
            {required: true, message: this.$t('commons.input_name'), trigger: 'blur'},
            {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
            {
              required: true,
              message: this.$t('commons.special_characters_are_not_supported'),
              trigger: 'blur'
            }
          ]
        },
        buttons: [
          {
            tip: this.$t('account.tuning'), icon: "el-icon-setting", type: "success",
            exec: this.handleScan
          }, {
            tip: this.$t('commons.edit'), icon: "el-icon-edit", type: "primary",
            exec: this.handleEdit
          }, {
            tip: this.$t('commons.delete'), icon: "el-icon-delete", type: "danger",
            exec: this.handleDelete
          }
        ],
        statusFilters: [
          {text: this.$t('account.INVALID'), value: 'INVALID'},
          {text: this.$t('account.VALID'), value: 'VALID'},
          {text: this.$t('account.DELETE'), value: 'DELETE'}
        ],
        cmOptions: {
          tabSize: 4,
          mode: {
            name: 'json',
            json: true
          },
          theme: 'bespin',
          lineNumbers: true,
          line: true,
          indentWithTabs: true,
        },
      }
    },

    watch: {
      '$route': 'init'
    },

    methods: {
      create() {
        this.form = {};
        this.tmpList = [];
        this.createVisible = true;
        this.activePlugin();
      },
      innerDrawerClose() {
        this.innerDrawer = false;
      },
      //校验云账号
      validate() {
        if (this.selectIds.size == 0) {
          this.$warning(this.$t('account.please_choose_account'));
          return;
        }
        this.$alert(this.$t('account.one_validate') + this.$t('account.cloud_account') + " ？", '', {
          confirmButtonText: this.$t('commons.confirm'),
          callback: (action) => {
            if (action === 'confirm') {
              let formData = new FormData();
              formData.append('selectIds', new Blob([JSON.stringify(Array.from(this.selectIds))], {
                type: "application/json"
              }));
              this.result = this.$request({
                method: 'POST',
                url: "/account/validate",
                data: formData,
                headers: {
                  'Content-Type': undefined
                }
              }, res => {
                this.search();
                if (res.success) {
                  this.$success(this.$t('account.success'));
                }
              });
            }
          }
        });
      },
      select(selection) {
        this.selectIds.clear()
        selection.forEach(s => {
          this.selectIds.add(s.id)
        })
      },
      //查询列表
      search() {
        let url = "/account/list/" + this.currentPage + "/" + this.pageSize;
        this.result = this.$post(url, this.condition, response => {
          let data = response.data;
          this.total = data.itemCount;
          this.tableData = data.listObject;
        });
      },
      handleEdit(tmp) {
        this.form = tmp;
        this.item.credential = tmp.credential;
        this.updateVisible = true;
        this.activePlugin();
        this.changePlugin(tmp.pluginId, 'edit');
      },
      handleClose() {
        this.createVisible =  false;
        this.updateVisible =  false;
        this.handleScan = false;
      },
      handleDelete(obj) {
        this.$alert(this.$t('account.delete_confirm') + obj.name + " ？", '', {
          confirmButtonText: this.$t('commons.confirm'),
          callback: (action) => {
            if (action === 'confirm') {
              this.result = this.$post("/account/delete/" + obj.id, {}, () => {
                this.$success(this.$t('commons.delete_success'));
                this.search();
              });
            }
          }
        });
      },
      change(e) {
        this.$forceUpdate();
      },
      handleCopy(test) {
        this.$refs.apiCopy.open(test);
      },
      showRegions (tmp) {
        this.regions = tmp.regions;
      },
      //调参云账号对应的规则
      handleScan(params) {
        this.$router.push({
          path: '/account/accountscan/' + params.id,
        }).catch(error => error);
      },
      //查询插件
      activePlugin() {
        let url = "/plugin/all";
        this.result = this.$get(url, response => {
          let data = response.data;
          this.plugins =  data;
        });
      },
      init() {
        this.selectIds.clear()
        this.search();
      },
      sort(column) {
        _sort(column, this.condition);
        this.init();
      },
      filter(filters) {
        _filter(filters, this.condition);
        this.init();
      },
      filterStatus(value, row) {
        return row.status === value;
      },
      //选择插件查询云账号信息
      async changePlugin (pluginId, type){
        this.$get("/account/iam/strategy/" + pluginId,res => {
          this.script = res.data;
        });
        let url = "/plugin/";
        this.result = await this.$get(url + pluginId, response => {
          let fromJson = typeof(response.data) == 'string'?JSON.parse(response.data):response.data;
          this.tmpList = fromJson.data;
          if (type === 'edit') {
            let credentials = typeof(this.item.credential) == 'string'?JSON.parse(this.item.credential):this.item.credential;
            for (let tmp of this.tmpList) {
              if (credentials[tmp.name] === undefined) {
                tmp.input = tmp.defaultValue?tmp.defaultValue:"";
              } else {
                tmp.input = credentials[tmp.name];
              }
            }
          } else {
            for (let tmp of this.tmpList) {
              if (tmp.defaultValue !== undefined) {
                tmp.input = tmp.defaultValue;
              }
            }
          }
        });
      },
      //保存云账号
      saveAccount(item, type){
        if (!this.tmpList.length) {
          this.$error(this.$t('account.i18n_account_cloud_plugin_param'));
          return;
        }
        this.$refs['accountForm'].validate(valid => {
          if (valid) {
            let data = {}, key = {};
            for (let tmp of this.tmpList) {
              key[tmp.name] = tmp.input;
            }
            data["credential"] = JSON.stringify(key);
            data["name"] = item.name;
            data["pluginId"] = item.pluginId;

            if (type === 'add') {
              this.result = this.$post("/account/add", data,response => {
                if (response.success) {
                  this.$success(this.$t('account.i18n_cs_create_success'));
                  this.search();
                  this.handleClose();
                } else {
                  this.$error(response.message);
                }
              });
            } else {
              data["id"] = item.id;
              this.result = this.$post("/account/update", data,response => {
                if (response.success) {
                  this.$success(this.$t('account.i18n_cs_update_success'));
                  this.handleClose();
                  this.search();
                } else {
                  this.$error(response.message);
                }
              });
            }
          } else {
            this.$error(this.$t('rule.full_param'));
            return false;
          }
        });
      },
      tableRowClassName({row, rowIndex}) {
        if (rowIndex % 4 === 0) {
          return 'success-row';
        } else if (rowIndex % 2 === 0) {
          return 'warning-row';
        } else {
          return '';
        }
      },
      scan (){
        if (this.selectIds.size == 0) {
          this.$warning(this.$t('account.please_choose_account'));
          return;
        }
        this.scanGroup();
      },
      scanGroup () {
        this.$alert(this.$t('account.one_scan') + this.$t('account.cloud_account') + " ？", '', {
          confirmButtonText: this.$t('commons.confirm'),
          callback: (action) => {
            if (action === 'confirm') {
              let formData = new FormData();
              formData.append('selectIds', new Blob([JSON.stringify(Array.from(this.selectIds))], {
                type: "application/json"
              }));
              this.result = this.$request({
                method: 'POST',
                url: "/rule/scan",
                data: formData,
                headers: {
                  'Content-Type': undefined
                }
              }, () => {
                for (let item of this.tableData) {
                  if (this.selectIds[0]===item.id) {
                    localStorage.setItem(ACCOUNT_ID, item.id);
                    localStorage.setItem(ACCOUNT_NAME, item.name);
                    break;
                  }
                }
                this.$success(this.$t('account.i18n_cs_create_success'));
                // this.$router.push({
                //   path: '/resource/result',
                // }).catch(error => error);
              });
            }
          }
        });
      }
    },
    created() {
      this.init();
    }

  }
</script>

<style scoped>
  .table-content {
    width: 100%;
  }

  .el-table {
    cursor: pointer;
  }

  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    padding: 10px 10%;
    width: 47%;
  }

  .rtl >>> .el-drawer__body {
    overflow-y: auto;
    padding: 20px;
  }
  .rtl >>> input {
    width: 100%;
  }
  .rtl >>> .el-select {
    width: 80%;
  }
  .rtl >>> .el-form-item__content {
    width: 60%;
  }
  .code-mirror {
    height: 600px !important;
  }
  .code-mirror >>> .CodeMirror {
    /* Set height, width, borders, and global font properties here */
    height: 600px !important;
  }
  /deep/ :focus{outline:0;}
</style>
