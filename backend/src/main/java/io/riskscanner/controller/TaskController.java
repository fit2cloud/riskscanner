package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.base.domain.TaskItemWithBLOBs;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.dto.QuartzTaskDTO;
import io.riskscanner.dto.TaskDTO;
import io.riskscanner.service.OrderService;
import io.riskscanner.service.TaskService;
import org.quartz.CronExpression;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "task")
public class TaskController {
    @Resource
    private TaskService taskService;
    @Resource
    private OrderService orderService;

    @GetMapping(value = "detail/{taskId}")
    public TaskDTO getTaskDetail(@PathVariable String taskId) {
        return orderService.getTaskDetail(taskId);
    }

    @GetMapping(value = "copy/{taskId}")
    public Object copy(@PathVariable String taskId) {
        return orderService.copy(taskId);
    }

    @GetMapping(value = "log/taskId/{taskId}")
    public Object getTaskItemLogByTask(@PathVariable String taskId) {
        return orderService.getTaskItemLogByTaskId(taskId);
    }

    @GetMapping(value = "quartz/log/taskId/{taskId}")
    public Object getQuartzLogByTask(@PathVariable String taskId) {
        return orderService.getQuartzLogByTask(taskId);
    }

    @PostMapping("quartz/log/{taskItemId}/{goPage}/{pageSize}")
    public Pager getquartzLogDetails(@PathVariable int goPage, @PathVariable int pageSize, @PathVariable String taskItemId) {
        TaskItemWithBLOBs taskItem = orderService.taskItemWithBLOBs(taskItemId);
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, orderService.getQuartzLogByTaskItemId(taskItem));
    }

    @GetMapping(value = "extendinfo/{taskId}")
    public TaskDTO getTaskExtendInfo(@PathVariable String taskId) {
        return orderService.getTaskExtendInfo(taskId);
    }

    @PostMapping(value = "retry/{taskId}")
    public void retryTask(@PathVariable String taskId) {
        orderService.retry(taskId);
    }

    @PostMapping(value = "getCronDesc/{taskId}")
    public void getCronDesc(@PathVariable String taskId) {
        orderService.getCronDesc(taskId);
    }

    @PostMapping("manual/list/{goPage}/{pageSize}")
    public Pager getManualTasks(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> param) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        param.put("type", "manual");
        return PageUtils.setPageInfo(page, taskService.selectQuartzTasks(param));
    }

    @PostMapping("manual/Alllist")
    public Object getAllManualTasks(@RequestBody Map<String, Object> param) {
        param.put("type", "manual");
        return taskService.selectQuartzTasks(param);
    }

    @GetMapping("manual/more/{taskId}")
    public Object morelTask(@PathVariable String taskId) throws Exception {
        return taskService.morelTask(taskId);
    }

    @PostMapping("manual/create")
    public Object saveManualTask(@RequestBody QuartzTaskDTO quartzTaskDTO) throws Exception {
        quartzTaskDTO.setType("manual");
        return taskService.saveManualTask(quartzTaskDTO);
    }

    @PostMapping("manual/delete")
    public void deleteManualTask(@RequestBody String quartzTaskId) throws Exception {
        taskService.deleteManualTask(quartzTaskId);
    }

    @PostMapping(value = "manual/dryRun")
    public Object dryRun(@RequestBody QuartzTaskDTO quartzTaskDTO) throws Exception {
        quartzTaskDTO.setType("manual");
        return taskService.dryRun(quartzTaskDTO);
    }

    @PostMapping("quartz/list/{goPage}/{pageSize}")
    public Pager getQuartzTasks(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> param) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        param.put("type", "quartz");
        return PageUtils.setPageInfo(page, taskService.selectQuartzTasks(param));
    }

    @PostMapping("quartz/Alllist")
    public Object getQuartzTasks(@RequestBody Map<String, Object> param) {
        param.put("type", "quartz");
        return taskService.selectQuartzTasks(param);
    }

    @PostMapping("quartz/create")
    public Object saveQuartzTask(@RequestBody QuartzTaskDTO quartzTaskDTO) throws Exception {
        quartzTaskDTO.setType("quartz");
        return taskService.saveQuartzTask(quartzTaskDTO);
    }

    @PostMapping("quartz/pause")
    public void pauseQuartzTask(@RequestBody String quartzTaskId) throws Exception {
        taskService.changeQuartzStatus(quartzTaskId, "pause");
    }

    @PostMapping("quartz/resume")
    public void resumeQuartzTask(@RequestBody String quartzTaskId) throws Exception {
        taskService.changeQuartzStatus(quartzTaskId, "resume");
    }

    @PostMapping("quartz/delete")
    public void deleteQuartzTask(@RequestBody String quartzTaskId) throws Exception {
        taskService.deleteQuartzTask(quartzTaskId);
    }

    @PostMapping(value = "quartz/dryRun")
    public Object quartzDryRun(@RequestBody QuartzTaskDTO quartzTaskDTO) throws Exception {
        quartzTaskDTO.setType("quartz");
        return taskService.dryRun(quartzTaskDTO);
    }

    @PostMapping(value = "quartz/validateCron")
    public Object validateCron(@RequestBody Map map) throws Exception {
        return CronExpression.isValidExpression(map.get("cron").toString());
    }
}
