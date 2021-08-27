<template>
  <div v-loading="result.loading">

    <el-radio-group v-model="selectType" style="margin-bottom: 15px;" @change="typeChange">
      <el-radio-button v-for="(item,index) in $store.getters.msgTypes.filter(type => type.pid <= 0)"
                       :key="index" class="de-msg-radio-class" :label="item.msgTypeId">
        {{ $t('webmsg.' + item.typeName) }}
      </el-radio-button>
    </el-radio-group>

    <el-table border class="adjust-table" :data="tableData" style="width: 100%" :row-class-name="tableRowClassName">
      <el-table-column prop="content" :label="$t('webmsg.content')">
        <template slot-scope="scope">

          <span style="display: flex;flex: 1;">
            <span>
              <i v-if="!scope.row.status" class="el-icon-message" style="color: #de6f6f;" />
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
        <template slot-scope="scope">
          <span>{{ getTypeName(scope.row.typeId) }}</span>
        </template>
      </el-table-column>

    </el-table>

  </div>
</template>

<script>

import {query, updateStatus} from '@/api/system/msg'
import bus from '@/common/js/bus'
import {addOrder, formatOrders} from '@/utils/index'

export default {
  components: {
  },
  data() {
    return {
      selectType: 0,
      result: {},
      msgTypes: [
        { value: 0, label: this.$t('webmsg.all_msg') },
        { value: 1, label: this.$t('webmsg.unread_msg') },
        { value: 2, label: this.$t('webmsg.read_msg') }
      ],
      data: [],
      columns: [],
      paginationConfig: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      },
      orderConditions: []
    }
  },
  mounted() {
    this.search()
  },
  created() {
  },
  methods: {
    select(selection) {
    },

    search() {
      const param = {}

      if (this.selectType >= 0) {
        param.type = this.selectType
      }

      if (this.orderConditions.length === 0) {
        param.orders = ['create_time desc ']
      } else {
        param.orders = formatOrders(this.orderConditions)
      }

      const { currentPage, pageSize } = this.paginationConfig
      query(currentPage, pageSize, param).then(response => {
        this.data = response.data.listObject
        this.paginationConfig.total = response.data.itemCount
      })
    },
    getTypeName(value) {
      return this.$t('webmsg.' + getTypeName(value))
    },
    typeChange(value) {
      this.search()
    },
    toDetail(row) {
      const param = { ...{ msgNotification: true, msgType: row.typeId, sourceParam: row.param }}
      if (this.hasPermissionRoute(row.router)) {
        this.$router.push({ name: row.router, params: param })
        row.status || this.setReaded(row)
        return
      }
      this.$warning(this.$t('commons.no_target_permission'))
    },
    hasPermissionRoute(name, permission_routes) {
      permission_routes = permission_routes || this.permission_routes
      for (let index = 0; index < permission_routes.length; index++) {
        const route = permission_routes[index]
        if (route.name && route.name === name) return true
        if (route.children && this.hasPermissionRoute(name, route.children)) return true
      }
      return false
    },
    // 设置已读
    setReaded(row) {
      this.$post('/msg/setReaded/' + row.msgId, {}, response => {
        bus.$emit('refresh-top-notification')
        this.search()
      });
    },
    sortChange({ column, prop, order }) {
      this.orderConditions = []
      if (!order) {
        this.search()
        return
      }
      if (prop === 'createTime') {
        prop = 'create_time'
      }
      if (prop === 'typeId') {
        prop = 'type_id'
      }
      addOrder({ field: prop, value: order }, this.orderConditions)
      this.search()
    }
  }

}
</script>

<style lang="scss" scoped>
.de-msg-radio-class {
  padding: 0 5px;
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
}
.de-msg-a:hover {
    text-decoration: underline !important;
    color: #0a7be0 !important;
    cursor: pointer !important;

}

</style>
