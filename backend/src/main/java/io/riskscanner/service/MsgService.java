package io.riskscanner.service;


import io.riskscanner.base.domain.*;
import io.riskscanner.base.mapper.MsgChannelMapper;
import io.riskscanner.base.mapper.MsgMapper;
import io.riskscanner.base.mapper.MsgSettingMapper;
import io.riskscanner.base.mapper.ext.ExtMsgMapper;
import io.riskscanner.commons.utils.CommonBeanFactory;
import io.riskscanner.commons.utils.SessionUtils;
import io.riskscanner.controller.handler.SubscribeNode;
import io.riskscanner.controller.request.msg.BatchSettingRequest;
import io.riskscanner.controller.request.msg.MsgRequest;
import io.riskscanner.controller.request.msg.MsgSettingRequest;
import io.riskscanner.dto.MsgGridDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MsgService {

    @Resource
    private MsgMapper msgMapper;

    @Resource
    private ExtMsgMapper extMsgMapper;

    @Resource
    private MsgChannelMapper msgChannelMapper;

    @Resource
    private MsgSettingMapper msgSettingMapper;

    public List<Msg> query(String userId, MsgRequest msgRequest) {
        String orderClause = " create_time desc";
        MsgExample example = new MsgExample();
        MsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);

        List<String> orders = msgRequest.getOrders();

        if (CollectionUtils.isNotEmpty(orders)) {
            orderClause = String.join(", ", orders);
        }

        if (ObjectUtils.isNotEmpty(msgRequest.getType())) {
            criteria.andTypeIdEqualTo(msgRequest.getType());
        }

        if (ObjectUtils.isNotEmpty(msgRequest.getStatus())) {
            criteria.andStatusEqualTo(msgRequest.getStatus());
        }

        example.setOrderByClause(orderClause);
        List<Msg> Msgs = msgMapper.selectByExample(example);
        return Msgs;
    }

    public List<MsgGridDto> queryGrid(String userId, MsgRequest msgRequest, List<Long> typeIds) {
        String orderClause = " create_time desc";
        MsgExample example = new MsgExample();
        MsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);

        List<String> orders = msgRequest.getOrders();

        if (CollectionUtils.isNotEmpty(orders)) {
            orderClause = String.join(", ", orders);
        }

        if (CollectionUtils.isNotEmpty(typeIds)){
            criteria.andTypeIdIn(typeIds);
        }

        if (ObjectUtils.isNotEmpty(msgRequest.getStatus())) {
            criteria.andStatusEqualTo(msgRequest.getStatus());
        }

        example.setOrderByClause(orderClause);
        List<MsgGridDto> msgGridDtos = extMsgMapper.queryGrid(example);
        return msgGridDtos;
    }

    public Long queryCount(String userId) {
        MsgExample example = new MsgExample();
        MsgExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId).andStatusEqualTo(false);
        return msgMapper.countByExample(example);
    }

    public void setReaded(Long msgId) {
        Msg msg = new Msg();
        msg.setMsgId(msgId);
        msg.setStatus(true);
        msg.setReadTime(System.currentTimeMillis());
        msgMapper.updateByPrimaryKeySelective(msg);
    }

    public void setBatchReaded(List<Long> msgIds) {
        extMsgMapper.batchStatus(msgIds, System.currentTimeMillis());
    }

    public void batchDelete(List<Long> msgIds) {
        extMsgMapper.batchDelete(msgIds);
    }

    public void save(Msg Msg) {
        msgMapper.insertSelective(Msg);
    }

    public List<MsgChannel> channelList() {
        MsgChannelExample example = new MsgChannelExample();
        return msgChannelMapper.selectByExample(example);
    }

    public List<MsgSetting> settingList() {
        String userId = SessionUtils.getUser().getId();
        MsgSettingExample example = new MsgSettingExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<MsgSetting> msgSettings = msgSettingMapper.selectByExample(example);
        msgSettings = addDefault(msgSettings);
        return msgSettings;
    }

    public List<MsgSetting> defaultSettings() {
        MsgSetting MsgSetting1 = new MsgSetting();
        MsgSetting1.setTypeId(2L);
        MsgSetting1.setChannelId(1L);
        MsgSetting1.setEnable(true);
        MsgSetting MsgSetting2 = new MsgSetting();
        MsgSetting2.setTypeId(6L);
        MsgSetting2.setChannelId(1L);
        MsgSetting2.setEnable(true);
        List<MsgSetting> lists = new ArrayList<>();
        lists.add(MsgSetting1);
        lists.add(MsgSetting2);
        return lists;
    }

    /**
     * 修改了订阅信息 需要清除缓存
     * @param request
     * @param userId
     */
    @Transactional
    public void updateSetting(MsgSettingRequest request, String userId) {
        Long typeId = request.getTypeId();
        Long channelId = request.getChannelId();
        MsgSettingExample example = new MsgSettingExample();
        example.createCriteria().andUserIdEqualTo(userId).andTypeIdEqualTo(typeId).andChannelIdEqualTo(channelId);
        List<MsgSetting> msgSettings = msgSettingMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(msgSettings)) {
            msgSettings.forEach(setting -> {
                setting.setEnable(!setting.getEnable());
                msgSettingMapper.updateByPrimaryKeySelective(setting);
            });
            return;
        }

        MsgSetting msgSetting = new MsgSetting();

        msgSetting.setChannelId(channelId);
        msgSetting.setTypeId(typeId);

        List<MsgSetting> defaultSettings = defaultSettings();

        msgSetting.setEnable(!defaultSettings.stream().anyMatch(setting -> setting.match(msgSetting)));

        msgSetting.setUserId(userId);

        msgSettingMapper.insertSelective(msgSetting);
    }


    @Transactional
    public void batchUpdate(BatchSettingRequest request, String userId) {
        // 先删除
        MsgSettingExample example = new MsgSettingExample();
        example.createCriteria().andUserIdEqualTo(userId).andChannelIdEqualTo(request.getChannelId()).andTypeIdIn(request.getTypeIds());
        msgSettingMapper.deleteByExample(example);
        // 再写入
        List<MsgSetting> settings = request.getTypeIds().stream().map(typeId -> {
            MsgSetting MsgSetting = new MsgSetting();
            MsgSetting.setUserId(userId);
            MsgSetting.setTypeId(typeId);
            MsgSetting.setChannelId(request.getChannelId());
            MsgSetting.setEnable(request.getEnable());
            return MsgSetting;
        }).collect(Collectors.toList());

        extMsgMapper.batchInsert(settings);
    }

    public void sendMsg(String userId, Long typeId, Long channelId, String content, String param) {
        Msg msg = new Msg();
        msg.setUserId(userId);
        msg.setTypeId(typeId);
        msg.setContent(content);
        msg.setStatus(false);
        msg.setCreateTime(System.currentTimeMillis());
        msg.setParam(param);
        save(msg);
    }

    /**
     * 查询用户订阅的消息 并缓存
     * @param userId
     * @return
     */
    public List<SubscribeNode> subscribes(String userId) {
        MsgSettingExample example = new MsgSettingExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<MsgSetting> msgSettings = msgSettingMapper.selectByExample(example);
        // 添加默认订阅
        msgSettings = addDefault(msgSettings);
        msgSettings = msgSettings.stream().filter(MsgSetting::getEnable).collect(Collectors.toList());
        List<SubscribeNode> resultLists = msgSettings.stream().map(item -> {
            SubscribeNode subscribeNode = new SubscribeNode();
            subscribeNode.setTypeId(item.getTypeId());
            subscribeNode.setChannelId(item.getChannelId());
            return subscribeNode;
        }).collect(Collectors.toList());
        return resultLists;
    }

    public List<MsgSetting> addDefault(List<MsgSetting> sourceLists) {
        List<MsgSetting> defaultSettings = defaultSettings();

        defaultSettings.forEach(setting -> {
            if (!sourceLists.stream().anyMatch(item -> item.match(setting))){
                sourceLists.add(setting);
            }
        });
        return sourceLists;
    }

}
