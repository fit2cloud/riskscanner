<template>
    <main-container>
      <el-card class="table-card" v-loading="result.loading">
        <template v-slot:header>
          <table-header :condition.sync="condition" @search="search"
                        :title="$t('rule.rule_tag_list')"
                        @create="create"
                        :createTip="$t('rule.create_tag')"
                        :show-create="true"/>

        </template>

        <el-table :border="true" :stripe="true" :data="tableData" class="adjust-table table-content" @sort-change="sort"
                  @filter-change="filter" @select-all="select" @select="select">
          <el-table-column min-width="1%"></el-table-column>
          <el-table-column prop="tagKey" :label="$t('rule.tag_key')" min-width="20%" show-overflow-tooltip></el-table-column>
          <el-table-column prop="tagName" :label="$t('rule.tag_name')" min-width="20%" show-overflow-tooltip></el-table-column>
          <el-table-column :label="$t('rule.tag_flag')" min-width="20%" show-overflow-tooltip>
            <template v-slot:default="scope">
              <el-tag size="mini" type="danger" v-if="scope.row.flag === true">
                {{ $t('rule.tag_flag_true') }}
              </el-tag>
              <el-tag size="mini" type="success" v-else-if="scope.row.flag === false">
                {{ $t('rule.tag_flag_false') }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="index" :label="$t('rule._index')" min-width="20%" show-overflow-tooltip></el-table-column>
          <el-table-column min-width="20%" :label="$t('commons.operating')">
            <template v-slot:default="scope">
              <table-operators v-if="!!scope.row.flag" :buttons="buttonsN" :row="scope.row"/>
              <table-operators v-if="!scope.row.flag" :buttons="buttons" :row="scope.row"/>
            </template>
          </el-table-column>
        </el-table>
        <table-pagination :change="search" :current-page.sync="currentPage" :page-size.sync="pageSize" :total="total"/>
      </el-card>

      <!--Create ruleTag-->
      <el-drawer class="rtl" :title="$t('rule.create_tag')" :visible.sync="createVisible" size="45%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="createForm" label-position="right" label-width="120px" size="small" :rules="rule" ref="createForm">
          <el-form-item :label="$t('rule.tag_key')" prop="tagKey">
            <el-input v-model="createForm.tagKey" autocomplete="off" :placeholder="$t('commons.please_input')"/>
          </el-form-item>
          <el-form-item :label="$t('rule.tag_name')" prop="tagName">
            <el-input v-model="createForm.tagName" autocomplete="off" :placeholder="$t('commons.please_input')"/>
          </el-form-item>
          <el-form-item :label="$t('rule._index')" prop="index">
            <el-input type="number" v-model="createForm.index" autocomplete="off" :placeholder="$t('commons.please_input')"/>
          </el-form-item>
        </el-form>
        <dialog-footer
          @cancel="createVisible = false"
          @confirm="save(createForm, 'add')"/>
      </el-drawer>
      <!--Create ruleTag-->

      <!--Update ruleTag-->
      <el-drawer class="rtl" :title="$t('rule.update_tag')" :visible.sync="updateVisible" size="45%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-form :model="updateForm" label-position="right" label-width="120px" size="small" :rules="rule" ref="updateForm">
            <el-form-item :label="$t('rule.tag_key')">
              <el-input v-model="updateForm.tagKey" :disabled="true" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
            <el-form-item :label="$t('rule.tag_name')">
              <el-input v-model="updateForm.tagName" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
            <el-form-item :label="$t('rule._index')">
              <el-input type="number" v-model="updateForm.index" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
          </el-form>
        <dialog-footer
          @cancel="updateVisible = false"
          @confirm="save(updateForm, 'update')"/>
      </el-drawer>
      <!--Update ruleTag-->

      <!--Info ruleTag-->
      <el-drawer class="rtl" :title="$t('rule.update_tag')" :visible.sync="infoVisible" size="45%" :before-close="handleClose" :direction="direction"
                 :destroy-on-close="true">
        <el-tooltip v-model="updateForm.flag" class="item" effect="dark" :content="$t('rule.rule_tag_flag')" placement="bottom">
          <el-form :model="updateForm" label-position="right" label-width="120px" size="small" :rules="rule" ref="updateForm">
            <el-form-item :label="$t('rule.tag_key')">
              <el-input v-model="updateForm.tagKey" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
            <el-form-item :label="$t('rule.tag_name')">
              <el-input v-model="updateForm.tagName" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
            <el-form-item :label="$t('rule._index')">
              <el-input type="number" v-model="updateForm.index" :disabled="updateForm.flag" autocomplete="off" :placeholder="$t('commons.please_input')"/>
            </el-form-item>
          </el-form>
        </el-tooltip>
      </el-drawer>
      <!--Info ruleTag-->

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
  import {RULE_TAG_CONFIGS} from "../../common/components/search/search-components";
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
          components: RULE_TAG_CONFIGS
        },
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
          tagKey: [
            {required: true, message: this.$t('rule.tag_key'), trigger: 'blur'},
            {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
            {
              required: true,
              message: this.$t('rule.special_characters_are_not_supported'),
              trigger: 'blur'
            }
          ],
          tagName: [
            {required: true, message: this.$t('rule.tag_name'), trigger: 'blur'},
            {min: 2, max: 50, message: this.$t('commons.input_limit', [2, 50]), trigger: 'blur'},
            {
              required: true,
              message: this.$t('rule.special_characters_are_not_supported'),
              trigger: 'blur'
            }
          ],
          index: [
            {
              required: true,
              pattern: '^[1-9]\\d*|0$',
              message: this.$t('rule.number_format_is_incorrect'),
              trigger: 'blur'
            }
          ],
        },
        buttons: [
          {
            tip: this.$t('commons.edit'), icon: "el-icon-edit", type: "primary",
            exec: this.handleEdit
          }, {
            tip: this.$t('commons.delete'), icon: "el-icon-delete", type: "danger",
            exec: this.handleDelete
          }
        ],
        buttonsN: [
          {
            tip: this.$t('commons.detail'), icon: "el-icon-edit-outline", type: "primary",
            exec: this.handleInfo
          }
        ],
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
          this.$warning(this.$t('rule.rule_tag_flag'));
          return;
        }
        this.$alert(this.$t('account.delete_confirm') + item.tagName + " ？", '', {
          confirmButtonText: this.$t('commons.confirm'),
          callback: (action) => {
            if (action === 'confirm') {
              this.result = this.$get("/tag/rule/delete/" + item.tagKey, () => {
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
      //查询列表
      search() {
        let url = "/rule/ruleTag/list/" + this.currentPage + "/" + this.pageSize;
        this.result = this.$post(url, this.condition, response => {
          let data = response.data;
          this.total = data.itemCount;
          this.tableData = data.listObject;
        });
      },
      init() {
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
      save(item, type) {
        let params = item;
        params.flag = item.flag ? item.flag : false;
        let url = type == "add" ? "/tag/rule/save" : "/tag/rule/update";
        this.result = this.$post(url, params, response => {
          this.search();
          this.createVisible =  false;
          this.updateVisible =  false;
          this.$success(this.$t('commons.opt_success'));
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
