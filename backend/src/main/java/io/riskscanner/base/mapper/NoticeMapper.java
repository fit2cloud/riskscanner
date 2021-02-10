package io.riskscanner.base.mapper;

import io.riskscanner.base.domain.Notice;
import io.riskscanner.base.domain.NoticeExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoticeMapper {
    long countByExample(NoticeExample example);

    int deleteByExample(NoticeExample example);

    int insert(Notice record);

    int insertSelective(Notice record);

    List<Notice> selectByExample(NoticeExample example);

    int updateByExampleSelective(@Param("record") Notice record, @Param("example") NoticeExample example);

    int updateByExample(@Param("record") Notice record, @Param("example") NoticeExample example);
}