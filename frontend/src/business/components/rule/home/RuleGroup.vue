<template>
    <main-container>
      <el-card class="table-card" v-loading="result.loading">
        <template v-slot:header>
          <table-header :condition.sync="condition" @search="search"
                        :title="$t('rule.rule_set_list')"
                        @create="create"
                        :createTip="$t('rule.create_rule_set')"
                        :show-create="true"/>

        </template>

        <el-table :border="true" :stripe="true" :data="tableData" class="adjust-table table-content" @sort-change="sort"
                  @filter-change="filter" @select-all="select" @select="select">
          <el-table-column type="index" min-width="5%"/>
          <el-table-column prop="name" :label="$t('rule.rule_set_name')" min-width="15%" show-overflow-tooltip></el-table-column>
          <el-table-column prop="description" :label="$t('commons.description')" min-width="50%" show-overflow-tooltip></el-table-column>
          <el-table-column :label="$t('account.cloud_platform')" min-width="10%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <span>
                <img :src="require(`@/assets/img/platform/${scope.row.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                 &nbsp;&nbsp; {{ scope.row.pluginName }}
              </span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('rule.tag_flag')" min-width="10%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <el-tag size="mini" type="danger" v-if="scope.row.flag === true">
                {{ $t('rule.tag_flag_true') }}
              </el-tag>
              <el-tag size="mini" type="success" v-else-if="scope.row.flag === false">
                {{ $t('rule.tag_flag_false') }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column min-width="10%" :label="$t('commons.operating')">
            <template v-slot:default="scope">
              <table-operators v-if="!!scope.row.flag" :buttons="buttonsN" :row="scope.row"/>
              <table-operators v-if="!scope.row.flag" :buttons="buttons" :row="scope.row"/>
            </template>
          </el-table-column>
        </el-table>
        <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
      </el-card>

      <!--Create group-->
      <el-drawer class="rtl" :title="$t('rule.create_group')" :visible.sync="createVisible" size="45%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="createForm" label-position="right" label-width="120px" size="small" :rules="rule" ref="createForm">
          <el-form-item :label="$t('rule.rule_set')" prop="name">
            <el-input v-model="createForm.name" autocomplete="off" :placeholder="$t('commons.please_input')"/>
          </el-form-item>
          <el-form-item :label="$t('commons.description')" prop="description">
            <el-input v-model="createForm.description" autocomplete="off" :placeholder="$t('commons.please_input')"/>
          </el-form-item>
          <el-form-item :label="$t('account.cloud_platform')" prop="pluginId" :rules="{required: true, message: $t('account.cloud_platform') + this.$t('commons.cannot_be_empty'), trigger: 'change'}">
            <el-select style="width: 100%;" v-model="createForm.pluginId" :placeholder="$t('account.please_choose_plugin')">
              <el-option
                v-for="item in plugins"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                &nbsp;&nbsp; {{ $t(item.name) }}
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <dialog-footer
          @cancel="createVisible = false"
          @confirm="save(createForm, 'createForm')"/>
      </el-drawer>
      <!--Create group-->

      <!--Update group-->
      <el-drawer class="rtl" :title="$t('rule.update_group')" :visible.sync="updateVisible" size="45%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="updateForm" label-position="right" label-width="120px" size="small" :rules="rule" ref="updateForm">
            <el-form-item :label="$t('rule.rule_set')" prop="name">
              <el-input v-model="updateForm.name" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
            <el-form-item :label="$t('commons.description')" prop="description">
              <el-input v-model="updateForm.description" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
          <el-form-item :label="$t('account.cloud_platform')" prop="pluginId" :rules="{required: true, message: $t('account.cloud_platform') + this.$t('commons.cannot_be_empty'), trigger: 'change'}">
            <el-select style="width: 100%;" v-model="updateForm.pluginId" :disabled="updateForm.flag" :placeholder="$t('account.please_choose_plugin')">
              <el-option
                v-for="item in plugins"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                &nbsp;&nbsp; {{ $t(item.name) }}
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <dialog-footer
          @cancel="updateVisible = false"
          @confirm="save(updateForm, 'updateForm')"/>
      </el-drawer>
      <!--Update group-->

      <!--Info group-->
      <el-drawer class="rtl" :title="$t('rule.update_group')" :visible.sync="infoVisible" size="45%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="updateForm" label-position="right" label-width="120px" size="small" :rules="rule" ref="infoForm">
          <el-form-item :label="$t('rule.rule_set')" prop="name">
            {{ updateForm.name }}
          </el-form-item>
          <el-form-item :label="$t('commons.description')" prop="description">
            {{ updateForm.description }}
          </el-form-item>
          <el-form-item :label="$t('account.cloud_platform')">
            <el-select style="width: 100%;" v-model="updateForm.pluginId" :disabled="updateForm.flag" :placeholder="$t('account.please_choose_plugin')">
              <el-option
                v-for="item in plugins"
                :key="item.id"
                :label="item.name"
                :value="item.id">
                <img :src="require(`@/assets/img/platform/${item.icon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                &nbsp;&nbsp; {{ $t(item.name) }}
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-drawer>
      <!--Info group-->
    </main-container>
</template>

<script>
  import TableOperators from "../../common/components/TableOperators";
  import MainContainer from "../../common/components/MainContainer";
  import Container from "../../common/components/Container";
  import TableHeader from "../../common/components/TableHeader";
  import TablePagination from "../../common/pagination/TablePagination";
  import TableOperator from "../../common/components/TableOperator";
  import DialogFooter from "../../common/components/DialogFooter";
  import {_filter, _sort} from "@/common/js/utils";
  import {RULE_GROUP_CONFIGS} from "../../common/components/search/search-components";
  import {LIST_CHANGE} from "@/business/components/common/head/ListEvent";

  export default {
    components: {
      TableOperators,
      MainContainer,
      Container,
      TableHeader,
      TablePagination,
      TableOperator,
      DialogFooter
    },
    data() {
      return {
        result: {},
        condition: {
          components: RULE_GROUP_CONFIGS
        },
        plugins: [],
        tableData: [],
        createForm: {},
        updateForm: {},
        createVisible: false,
        updateVisible: false,
        infoVisible: false,
        currentPage: 1,
        pageSize: 10,
        total: 0,
        loading: false,
        selectIds: new Set(),
        direction: 'rtl',
        rule: {
          name: [
            {required: true, message: this.$t('rule.tag_name') + this.$t('commons.cannot_be_empty'), trigger: 'blur'},
            {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
            {
              required: true,
              message: this.$t('rule.special_characters_are_not_supported'),
              trigger: 'blur'
            }
          ],
          description: [
            {required: true, message: this.$t('rule.description') + this.$t('commons.cannot_be_empty'), trigger: 'blur'},
            {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
            {
              required: true,
              message: this.$t('rule.special_characters_are_not_supported'),
              trigger: 'blur'
            }
          ]
        },
        buttonsN: [
          {
            tip: this.$t('commons.detail'), icon: "el-icon-edit-outline", type: "primary",
            exec: this.handleInfo
          }
        ],
        buttons: [
          {
            tip: this.$t('commons.edit'), icon: "el-icon-edit", type: "primary",
            exec: this.handleEdit
          }, {
            tip: this.$t('commons.delete'), icon: "el-icon-delete", type: "danger",
            exec: this.handleDelete
          }
        ]
      }
    },

    watch: {
      '$route': 'init'
    },

    methods: {
      create() {
        this.createForm = {};
        this.createVisible = true;
      },
      handleInfo(item) {
        this.updateForm = {};
        this.updateForm = item;
        this.infoVisible = true;
      },
      handleEdit(item) {
        this.updateForm = {};
        this.updateForm = item;
        this.updateVisible = true;
      },
      handleClose() {
        this.createVisible =  false;
        this.updateVisible =  false;
        this.infoVisible =  false;
        this.search();
      },
      handleDelete(item) {
        if (item.flag) {
          this.$warning(this.$t('rule.rule_group_flag'));
          return;
        }
        this.$alert(this.$t('account.delete_confirm') + item.name + " ？", '', {
          confirmButtonText: this.$t('commons.confirm'),
          callback: (action) => {
            if (action === 'confirm') {
              this.result = this.$get("/rule/group/delete/" + item.id, () => {
                this.$success(this.$t('commons.delete_success'));
                this.search();
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
      getPlugins () {
        this.result = this.$get("/plugin/all", response => {
          this.plugins = response.data;
        });
      },
      //查询列表
      search() {
        let url = "/rule/ruleGroup/list/" + this.currentPage + "/" + this.pageSize;
        this.result = this.$post(url, this.condition, response => {
          let data = response.data;
          this.total = data.itemCount;
          this.tableData = data.listObject;
        });
      },
      init() {
        this.search();
        this.getPlugins();
      },
      sort(column) {
        _sort(column, this.condition);
        this.init();
      },
      filter(filters) {
        _filter(filters, this.condition);
        this.init();
      },
      save(item, type) {
        this.$refs[type].validate(valid => {
            if (valid) {
              let params = item;
              params.flag = item.flag ? item.flag : false;
              let url = type == "createForm" ? "/rule/group/save" : "/rule/group/update";
              this.result = this.$post(url, params, response => {
                this.search();
                this.createVisible =  false;
                this.updateVisible =  false;
                this.$success(this.$t('commons.opt_success'));
              });
            }
        });
      },
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
  /deep/ :focus{outline:0;}
</style>
