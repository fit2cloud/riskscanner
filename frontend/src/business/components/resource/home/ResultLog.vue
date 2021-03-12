<template>
  <div>
    <el-row class="el-form-item-dev" v-if="row.taskItemLogDTOs.length == 0">
      <span>{{$t('resource.i18n_no_data')}}<br></span>
    </el-row>
    <el-row class="el-form-item-dev" v-if="row.taskItemLogDTOs.length > 0">
      <el-table :show-header="false" :data="row.taskItemLogDTOs" class="adjust-table table-content">
        <el-table-column>
          <template v-slot:default="scope">
            <div>
              <el-row>
                <el-col :span="24">
                  <div class="grid-content bg-purple-light">
                    <span class="grid-content-log-span"> {{scope.row.rule.name}}</span>
                    <span class="grid-content-log-span">
                      <img :src="require(`@/assets/img/platform/${scope.row.rule.pluginIcon}`)" style="width: 16px; height: 16px; vertical-align:middle" alt=""/>
                       &nbsp;&nbsp; {{scope.row.rule.pluginName}} | {{scope.row.taskItem.regionName}}
                    </span>
                    <span class="grid-content-status-span" v-if="scope.row.taskItem.status === 'UNCHECKED'" style="color: #919398">
                      <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}...
                    </span>
                    <span class="grid-content-status-span" v-else-if="scope.row.taskItem.status === 'APPROVED'" style="color: #579df8">
                      <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}...
                    </span>
                    <span class="grid-content-status-span" v-else-if="scope.row.taskItem.status === 'PROCESSING'" style="color: #579df8">
                      <i class="el-icon-loading"></i> {{ $t('resource.i18n_in_process') }}...
                    </span>
                    <span class="grid-content-status-span" v-else-if="scope.row.taskItem.status === 'FINISHED'" style="color: #7ebf50">
                      <i class="el-icon-success"></i> {{ $t('resource.i18n_done') }}
                    </span>
                    <span class="grid-content-status-span" v-else-if="scope.row.taskItem.status === 'ERROR'" style="color: red">
                      <i class="el-icon-error"></i> {{ $t('resource.i18n_has_exception') }}
                    </span>
                    <span class="grid-content-status-span" v-else-if="scope.row.taskItem.status === 'WARNING'" style="color: #dda450">
                      <i class="el-icon-warning"></i> {{ $t('resource.i18n_has_warn') }}
                    </span>
                </div>
                </el-col>
              </el-row>
            </div>
            <div class="bg-purple-div">
              <span v-for="(logItem, index) in scope.row.taskItemLogList" :key="index"
                    v-bind:class="{true: 'color-red', false: ''}[logItem.result == false]">
                    {{logItem.createTime | timestampFormatDate}}
                    {{logItem.operator}}
                    {{logItem.operation}}
                    {{logItem.output}}<br>
              </span>
              <div v-if="(scope.row.taskItem.status === 'FINISHED' || scope.row.taskItem.status === 'ERROR' || scope.row.taskItem.status === 'WARNING')
                      && (scope.row.taskItemLogList.length === 0)">
                {{ $t('resource.the_cloud_platform') }}
                {{scope.row.taskItem.accountLabel}} |
                {{scope.row.taskItem.regionName}}
                {{ $t('resource.not_currently') }}
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
  </div>
</template>

<script>

  export default {
    name: "ResultLog",
    components: {
    },
    data() {
      return {
        timer: '',
      }
    },
    activated() {
    },
    props: {
      row: {taskItemLogDTOs: []},
    },
    methods: {
      init() {
      },
      getStatus () {
        if (this.checkStatus(this.row.taskItemLogDTOs)) {
          return;
        }
        let showLogTaskId = this.row.showLogTaskId;
        let url = "/task/log/taskId/";
        this.row.taskItemLogDTOs = [];
        this.$get(url + showLogTaskId, response => {
          this.row.taskItemLogDTOs = response.data;
        });
      },
      //是否是结束状态，返回false代表都在运行中，true代表已结束
      checkStatus (taskItemLogDTOs) {
        let sum = 0;
        for (let row of taskItemLogDTOs) {
          if (row.taskItem.status != 'ERROR' && row.taskItem.status != 'FINISHED' && row.taskItem.status != 'WARNING') {
            sum++;
          }
        }
        return sum == 0;
      },
    },

    created() {
      this.init();
    },
    mounted() {
      this.timer = setInterval(this.getStatus,5000);
    },
    beforeDestroy() {
      clearInterval(this.timer);
    }
  }
</script>

<style scoped>
  .el-row {
    margin-bottom: 20px;
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #f2f2f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }

  /*.el-form-item-dev  >>> .el-form-item__content {*/
  /*  margin-left: 0 !important;*/
  /*}*/

  .grid-content-log-span {
    width: 38%;
    float: left;
    vertical-align: middle;
    display:table-cell;
    margin: 6px 1%;
  }

  .grid-content-status-span {
    width: 18%;float: left;
    vertical-align: middle;
    display:table-cell;
    margin: 6px 1%;
  }

  .bg-purple-div {
    margin: 10px;
  }

</style>
