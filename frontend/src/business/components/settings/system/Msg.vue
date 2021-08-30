<template>
  <div v-loading="result.loading">

    <el-radio-group v-model="selectType" style="margin-bottom: 15px;" @change="typeChange">
      <el-radio-button v-for="(item,index) in msgTypes" :key="index" class="de-msg-radio-class" :label="item.value">
        {{ $t(item.label) }}
      </el-radio-button>
    </el-radio-group>
    <el-row style="margin: 0 5px 10px 5px;">
      <el-button size="mini" :disabled="selectIds.length===0" @click="markReaded">{{ $t('webmsg.mark_readed') }}</el-button>
      <el-button size="mini" :disabled="selectIds.length===0" @click="deleteBatch">{{ $t('commons.delete') }}</el-button>
    </el-row>

    <el-table border class="adjust-table" :data="tableData" style="width: 100%" :row-class-name="tableRowClassName"
              @select-all="select" @select="select">
      <el-table-column type="selection" min-width="5%">
      </el-table-column>
      <el-table-column prop="content" :label="$t('webmsg.content')">
        <template slot-scope="scope">
          <span style="display: flex;flex: 1;">
            <span>
              <i v-if="!scope.row.status" class="el-icon-message" style="color: red;" />
              <i v-else class="el-icon-message"/>
            </span>
            <span style="margin-left: 6px;" class="de-msg-a" @click="toDetail(scope.row)">
              {{ scope.row.content }}
            </span>
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" sortable="custom" :label="$t('webmsg.sned_time')" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | timestampFormatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="typeId" sortable="custom" :label="$t('webmsg.type')" width="140">
          <span>{{ 'xxx' }}</span>
      </el-table-column>

    </el-table>

  </div>
</template>

<script>

import bus from '@/common/js/bus'

export default {
  name: "Msg",
  components: {
  },
  data() {
    return {
      selectType: 0,
      result: {},
      msgTypes: [
        { value: null, label: this.$t('webmsg.all_msg') },
        { value: 0, label: this.$t('webmsg.unread_msg') },
        { value: 1, label: this.$t('webmsg.read_msg') }
      ],
      tableData: [],
      columns: [],
      paginationConfig: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      orderConditions: [],
      form: {},
      selectIds: [],
      loading: false,
    }
  },
  mounted() {
    this.selectIds = [];
    this.search()
  },
  created() {
  },
  methods: {
    select(selection) {
      this.selectIds = [];
      selection.forEach(s => {
        this.selectIds.push(s.msgId);
      });
    },
    search() {
      const { currentPage, pageSize } = this.paginationConfig;
      this.result = this.$post('/msg/list/' + currentPage + '/' + pageSize, this.form, response => {
        this.tableData = response.data.listObject;
        this.paginationConfig.total = response.data.itemCount;
      });
    },
    typeChange(value) {
      if(value!=null){
        this.form.status = value;
      }else{
        this.form = {};
      }
      this.search();
    },
    toDetail(row) {
    },
    // 设置已读
    setReaded(row) {
      this.$post('/msg/setReaded/' + row.msgId, {}, response => {
        bus.$emit('refresh-top-notification');
        this.search();
      });
    },
    markReaded() {
      if (this.selectIds.length === 0) {
        this.$warning(this.$t('webmsg.please_select'));
        return;
      }
      const param = this.selectIds;
      this.$post('/msg/batchRead', param, response => {
        this.search();
      });
    },
    deleteBatch() {
      if (this.selectIds.length === 0) {
        this.$warning(this.$t('webmsg.please_select'));
        return;
      }
      const param = this.selectIds;
      this.$post('/msg/batchDelete', param, response => {
        this.search();
      });
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
  }

}
</script>

<style scoped>
.de-msg-radio-class {
  padding: 0 5px;
}
>>>.el-radio-button__inner {
  border-radius: 4px 4px 4px 4px !important;
  border-left: 1px solid #dcdfe6 !important;
  padding: 10px 10px;
}
>>>.el-radio-button__orig-radio:checked+.el-radio-button__inner {
  color: #fff;
  background-color: #0a7be0;
  border-color: #0a7be0;
  -webkit-box-shadow: 0px 0 0 0 #0a7be0;
  box-shadow: 0px 0 0 0 #0a7be0;
}
.de-msg-a:hover {
    text-decoration: underline !important;
    color: #0a7be0 !important;
    cursor: pointer !important;

}

</style>
