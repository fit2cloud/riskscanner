package io.riskscanner.base.mapper.ext;

import io.riskscanner.controller.request.QueryScheduleRequest;
import io.riskscanner.dto.ScheduleDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtScheduleMapper {
    List<ScheduleDao> list(@Param("request") QueryScheduleRequest request);
}