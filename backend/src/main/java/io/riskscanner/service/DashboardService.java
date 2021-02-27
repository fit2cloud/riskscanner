package io.riskscanner.service;

import io.riskscanner.base.domain.ScanHistory;
import io.riskscanner.base.mapper.ext.ExtVulnMapper;
import io.riskscanner.base.rs.ChartData;
import io.riskscanner.base.rs.DashboardTarget;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Resource
    private ExtVulnMapper extVulnMapper;

    public List<ChartData> vulnDistribution(Map<String, Object> params) {

        final String group = MapUtils.getString(params, "group");
        switch (group) {
            case "overall":
                return extVulnMapper.overall(params);
            case "ruleGroup":
                return extVulnMapper.ruleGroup(params);
            case "report":
                return extVulnMapper.report(params);
            case "ruleList":
                return extVulnMapper.ruleList(params);
            case "accountList":
                return extVulnMapper.accountList(params);
            case "regionsList":
                return extVulnMapper.regionsList(params);
            default:
                return new LinkedList<>();
        }
    }

    public List<Map<String, Object>> severityList(Map<String, Object> params) {
        return extVulnMapper.severityList(params);
    }

    public List<Map<String, Object>> totalPolicy(Map<String, Object> params) {
        return extVulnMapper.totalPolicy(params);
    }

    public List<DashboardTarget> target(Map<String, Object> params) {
        return extVulnMapper.target(params);
    }

    public List<ScanHistory> history(Map<String, Object> params) {
        return extVulnMapper.history(params);
    }

}

