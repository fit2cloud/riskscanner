package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.controller.request.log.OperayionLogRequest;
import io.riskscanner.service.OperationLogService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("log/operation")
public class OperationLogController {
    @Resource
    private OperationLogService operationLogService;

    @RequestMapping("query/resource/{goPage}/{pageSize}")
    public Pager queryOperationLog(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody OperayionLogRequest dto) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, operationLogService.selectOperationLog(dto));
    }

    @RequestMapping("query/resource/{resourceId}/{goPage}/{pageSize}")
    public Pager queryResourceOperationLog(@PathVariable String resourceId, @PathVariable int goPage, @PathVariable int pageSize) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, operationLogService.selectRersourceOperationLog(resourceId));
    }

    @RequestMapping("query/user/{userId}/{goPage}/{pageSize}")
    public Pager queryUserOperationLog(@PathVariable String userId, @PathVariable int goPage, @PathVariable int pageSize) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, operationLogService.selectRersourceOperationLog(userId));
    }


    @RequestMapping("query/workspace/{workspaceId}/{goPage}/{pageSize}")
    public Pager queryWorkspaceOperationLog(@PathVariable String workspaceId, @PathVariable int goPage, @PathVariable int pageSize) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, operationLogService.selectRersourceOperationLog(workspaceId));
    }


}
