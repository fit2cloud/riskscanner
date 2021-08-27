package io.riskscanner.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.riskscanner.base.domain.MsgChannel;
import io.riskscanner.base.domain.MsgSetting;
import io.riskscanner.commons.utils.PageUtils;
import io.riskscanner.commons.utils.Pager;
import io.riskscanner.commons.utils.SessionUtils;
import io.riskscanner.controller.request.msg.BatchSettingRequest;
import io.riskscanner.controller.request.msg.MsgRequest;
import io.riskscanner.controller.request.msg.MsgSettingRequest;
import io.riskscanner.dto.MsgGridDto;
import io.riskscanner.service.MsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "系统：消息管理")
@RequestMapping("/api/msg")
@RestController
public class MsgController {

    @Resource
    private MsgService msgService;

    @ApiOperation("分页查询")
    @PostMapping("/list/{goPage}/{pageSize}")
    public Pager<List<MsgGridDto>> messages(@PathVariable int goPage, @PathVariable int pageSize, @RequestBody MsgRequest msgRequest) {
        String userId = SessionUtils.getUser().getId();
        List<Long> typeIds = null;
        Page<Object> page = PageHelper.startPage(goPage, pageSize, true);
        Pager<List<MsgGridDto>> listPager = PageUtils.setPageInfo(page, msgService.queryGrid(userId, msgRequest, typeIds));
        return listPager;
    }

    @ApiOperation("查询未读数量")
    @PostMapping("/unReadCount")
    public Long unReadCount(@RequestBody Map<String, String> request) {
        if(null == request || null == request.get("userId")) {
            throw new RuntimeException("缺少用户ID");
        }
        String userId = request.get("userId");
        return msgService.queryCount(userId);
    }

    @ApiOperation("设置已读")
    @PostMapping("/setReaded/{msgId}")
    public void setReaded(@PathVariable Long msgId) {
        msgService.setReaded(msgId);
    }


    @ApiOperation("批量设置已读")
    @PostMapping("/batchRead")
    public void batchRead(@RequestBody List<Long> msgIds) {
        msgService.setBatchReaded(msgIds);
    }

    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    public void batchDelete(@RequestBody List<Long> msgIds) {
        msgService.batchDelete(msgIds);
    }

    @ApiOperation("查询渠道")
    @PostMapping("/channelList")
    public List<MsgChannel> channelList() {
        return msgService.channelList();
    }

    @ApiOperation("查询订阅")
    @PostMapping("/settingList")
    public List<MsgSetting> settingList() {
        return msgService.settingList();
    }

    @ApiOperation("更新订阅")
    @PostMapping("/updateSetting")
    public void updateSetting(@RequestBody MsgSettingRequest request) {
        String userId = SessionUtils.getUser().getId();
        msgService.updateSetting(request, userId);
    }

    @ApiOperation("批量更新订阅")
    @PostMapping("/batchUpdate")
    public void batchUpdate(@RequestBody BatchSettingRequest request) {
        String userId = SessionUtils.getUser().getId();
        msgService.batchUpdate(request, userId);
    }
}
