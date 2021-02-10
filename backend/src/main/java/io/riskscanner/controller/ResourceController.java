package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.riskscanner.base.domain.Account;
import io.riskscanner.base.domain.AccountWithBLOBs;
import io.riskscanner.base.domain.ResourceWithBLOBs;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.controller.request.excel.ExcelExportRequest;
import io.riskscanner.controller.request.resource.JsonRequest;
import io.riskscanner.controller.request.resource.ResourceRequest;
import io.riskscanner.dto.ReportDTO;
import io.riskscanner.dto.ResourceDTO;
import io.riskscanner.dto.ResourceDetailDTO;
import io.riskscanner.dto.SourceDTO;
import io.riskscanner.service.ResourceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author maguohao
 */
@RestController
@RequestMapping("resource")
public class ResourceController {
    @Resource
    private ResourceService resourceService;

    @GetMapping("source/{accountId}")
    public SourceDTO sourceDTO(@PathVariable String accountId) {
        return resourceService.source(accountId);
    }

    @PostMapping("list/{goPage}/{pageSize}")
    public Pager<List<ResourceDTO>> list(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ResourceRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, resourceService.search(request));
    }

    @PostMapping("reportList/{goPage}/{pageSize}")
    public Pager<List<ReportDTO>> reportList(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody ResourceRequest request) {
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        return PageUtils.setPageInfo(page, resourceService.reportList(request));
    }

    @GetMapping("{id}")
    public ResourceDetailDTO getResource(@PathVariable String id) {
        return resourceService.getResource(id);
    }

    @PostMapping("getElement")
    public String getElement(@RequestBody String element) {
        String result = "";
        try{
            result = resourceService.toJSONString2(element);
        }catch (Exception e) {
            result = resourceService.toJSONString(element);
        }
        return result;
    }

    @GetMapping("fix/{id}")
    public ResourceWithBLOBs fixResource(@PathVariable String id) throws Exception {
        return resourceService.operatingResource(id, "fix");
    }

    @GetMapping("restart/{id}")
    public ResourceWithBLOBs restartResource(@PathVariable String id) throws Exception {
        return resourceService.operatingResource(id, "restart");
    }

    @GetMapping(value = "log/resourceId/{resourceId}")
    public Object getResourceLog(@PathVariable String resourceId) {
        return resourceService.getResourceLog(resourceId);
    }

    @PostMapping("export")
    public ResponseEntity<byte[]> exportCloudServers(@RequestBody ExcelExportRequest request) throws Exception {
        byte[] bytes = resourceService.export(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Export");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .body(bytes);
    }

    @PostMapping("string2PrettyFormat")
    public String string2PrettyFormat(@RequestBody JsonRequest jsonRequest) throws Exception {
        try {
            return resourceService.toJSONString(jsonRequest.getJson());
        } catch (Exception e) {
            return resourceService.toJSONString2(jsonRequest.getJson());
        }
    }

    @GetMapping("account/delete/{id}")
    public void deleteResourceByAccountId(@PathVariable String id) throws Exception {
        resourceService.deleteResourceByAccountId(id);
    }

    @GetMapping("report/iso/{accountId}/{groupId}")
    public Map<String, String> reportIso(@PathVariable String accountId, @PathVariable String groupId) throws Exception {
        return resourceService.reportIso(accountId, groupId);
    }

    @PostMapping("rule/groups")
    public Object groups(@RequestBody Map<String, Object> params) {
        return resourceService.groups(params);
    }
}
