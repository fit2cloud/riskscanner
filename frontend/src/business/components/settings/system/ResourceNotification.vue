<template>
  <div>
    <el-row>
      <el-col :span="10">
        <h3>{{ $t('system_parameter_setting.message.task_notification') }}</h3>
        <el-button icon="el-icon-circle-plus-outline" plain size="mini" @click="handleAddTaskModel">
          {{ $t('system_parameter_setting.message.create_new_notification') }}
        </el-button>
        <el-popover placement="right-end" title="示例" width="400" trigger="click" :content="content">
          <el-button icon="el-icon-warning" plain size="mini" slot="reference">
            {{ $t('system_parameter_setting.message.mail_template_example') }}
          </el-button>
        </el-popover>
      </el-col>
    </el-row>


    <el-row>
      <el-col :span="24">
        <el-table :data="resourceTask" class="tb-edit" border :cell-style="rowClass" :header-cell-style="headClass">
          <el-table-column :label="$t('schedule.event')" min-width="15%" prop="events">
            <template slot-scope="scope">
              <el-select v-model="scope.row.event" :placeholder="$t('system_parameter_setting.message.select_events')"
                         size="mini" prop="events" :disabled="!scope.row.isSet">
                <el-option v-for="item in eventOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('schedule.receiver')" prop="userIds" min-width="20%">
            <template v-slot:default="{row}">
              <el-select v-model="row.userIds" filterable multiple size="mini"
                         :placeholder="$t('commons.please_select')" style="width: 100%;" :disabled="!row.isSet">
                <el-option v-for="item in receiverOptions" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('schedule.receiving_mode')" min-width="20%" prop="type">
            <template slot-scope="scope">
              <el-select v-model="scope.row.type" :placeholder="$t('system_parameter_setting.message.select_receiving_method')"
                         size="mini" :disabled="!scope.row.isSet" @change="handleEdit(scope.$index, scope.row)">
                <el-option v-for="item in receiveTypeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column :label="$t('commons.operating')" min-width="25%" prop="result">
            <template v-slot:default="scope">
              <el-button type="primary" size="mini" v-if="scope.row.isSet" @click="handleAddTask(scope.$index,scope.row)">
                {{ $t('commons.add') }}
              </el-button>
              <el-button size="mini" v-if="scope.row.isSet" @click.native.prevent="removeRowTask(scope.$index,resourceTask)">
                {{ $t('commons.cancel') }}
              </el-button>
              <el-button type="primary" size="mini" v-if="!scope.row.isSet" @click="handleEditTask(scope.$index,scope.row)">
                {{ $t('commons.edit') }}
              </el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" v-show="!scope.row.isSet"
                @click.native.prevent="deleteRowTask(scope.$index,scope.row)"></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>

/* eslint-disable */
export default {
  name: "ResourceNotification",
  components: {
  },
  props: {
    receiverOptions: {
      type: Array
    }
  },
  data() {
    return {
      content: '<!DOCTYPE html>\n' +
        '<html lang="en">\n' +
        '<head>\n' +
        '    <meta charset="UTF-8">\n' +
        '    <title>RiskScanner</title>\n' +
        '</head>\n' +
        '<body>\n' +
        '<div>\n' +
        '    <div style="text-align: left">\n' +
        '        <p>尊敬的用户：</p>\n' +
        '        <p style="margin-left: 60px">您好, 您的安全合规扫描结果如下:\n' +
        '    </div>\n' +
        '    <div style="margin-left: 100px">\n' +
        '        <table width="90%">\n' +
        '            <thead>' +
        '            <tr class="alter">' +
        '               <th>资源标识</th>' +
        '               <th>资源类型</th>' +
        '               <th>云账号</th>' +
        '               <th>区域</th>' +
        '               <th>规则名称</th>' +
        '               <th>风险等级</th>' +
        '            </tr>' +
        '            </thead>' +
        '        </table">\n' +
        '    </div>\n' +
        '\n' +
        '</div>\n' +
        '</body>\n' +
        '</html>',
      resourceTask: [{
        taskType: "RESOURCE_TASK",
        event: "",
        userIds: [],
        type: [],
        isSet: true,
        identification: "",
        isReadOnly: false,
      }],
      eventOptions: [
        {value: 'EXECUTE_SUCCESSFUL', label: this.$t('schedule.event_success')},
        {value: 'EXECUTE_FAILED', label: this.$t('schedule.event_failed')}
      ],
      receiveTypeOptions: [
        {value: 'EMAIL', label: this.$t('system_parameter_setting.message.mail')},
        // {value: 'NAIL_ROBOT', label: this.$t('system_parameter_setting.message.nail_robot')},
        // {value: 'WECHAT_ROBOT', label: this.$t('system_parameter_setting.message.enterprise_wechat_robot')}
      ],
    }
  },
  activated() {
    this.initForm();
  },
  methods: {
    initForm() {
      this.result = this.$get('/notice/search/message/type/' + "RESOURCE_TASK", response => {
        this.resourceTask = response.data;
      })
    },
    handleEdit(index, data) {
      data.isReadOnly = true;
      if (data.type === 'EMAIL') {
        data.isReadOnly = !data.isReadOnly;
      }
    },
    handleAddTaskModel() {
      let Task = {};
      Task.event = [];
      Task.userIds = [];
      Task.type = '';
      Task.isSet = true;
      Task.identification = '';
      Task.taskType = "RESOURCE_TASK";
      this.resourceTask.push(Task)
    },
    handleAddTask(index, data) {
      if (data.event && data.userIds.length > 0 && data.type) {
        if (data.type === 'NAIL_ROBOT' || data.type === 'WECHAT_ROBOT') {
          this.addTask(data)
        } else {
          this.addTask(data)
        }
      } else {
        this.$warning(this.$t('system_parameter_setting.message.message'));
      }
    },
    handleEditTask(index, data) {
      data.isSet = true;
      if (data.type === 'EMAIL') {
        data.isReadOnly = false;
      } else {
        data.isReadOnly = true;
      }
    },
    addTask(data) {
      this.result = this.$post("/notice/save/message/task", data, () => {
        this.initForm();
        this.$success(this.$t('commons.save_success'));
      })
    },
    removeRowTask(index, data) { //移除
      if (!data[index].identification) {
        data.splice(index, 1)
      } else {
        data[parseInt(index)].isSet = false;
      }

    },
    deleteRowTask(index, data) { //删除
      this.result = this.$get("/notice/delete/message/" + data.identification, response => {
        this.$success(this.$t('commons.delete_success'));
        this.initForm()
      })
    },
    rowClass() {
      return "text-align:center"
    },
    headClass() {
      return "text-align:center;background:'#ededed'"
    },
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 10px;
}

.el-button {
  margin-left: 10px;
}
</style>
