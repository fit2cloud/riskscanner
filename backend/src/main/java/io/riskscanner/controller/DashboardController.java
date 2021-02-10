package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.service.DashboardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @PostMapping("point/target/{goPage}/{pageSize}")
    public Object target(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, dashboardService.target(params));
    }

    @PostMapping("point/severity")
    public Object severityList(@RequestBody Map<String, Object> params) {
        return dashboardService.severityList(params);
    }

    @PostMapping("distribution")
    public Object getDistribution(@RequestBody Map<String, Object> params) {
        return dashboardService.vulnDistribution(params);
    }

    @PostMapping("totalPolicy")
    public Object totalPolicy(@RequestBody Map<String, Object> params) {
        return dashboardService.totalPolicy(params);
    }

    @PostMapping("history/{goPage}/{pageSize}")
    public Object history(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody Map<String, Object> params) {
        Page page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, dashboardService.history(params));
    }


}
