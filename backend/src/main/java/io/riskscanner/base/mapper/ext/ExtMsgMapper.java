package io.riskscanner.base.mapper.ext;

import io.riskscanner.base.domain.MsgExample;
import io.riskscanner.base.domain.MsgSetting;
import io.riskscanner.dto.MsgGridDto;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ExtMsgMapper {


    @Update({
        "<script>",
            "update msg set status = 1, read_time = #{time} where msg_id in ",
            "<foreach collection='msgIds' item='msgId' open='(' separator=',' close=')' >",
                " #{msgId}",
            "</foreach>",
        "</script>"
    })
    int batchStatus(@Param("msgIds") List<Long> msgIds, @Param("time") Long time);


    @Delete({
            "<script>",
                "delete from msg where msg_id in ",
                "<foreach collection='msgIds' item='msgId' open='(' separator=',' close=')' >",
                    " #{msgId}",
                "</foreach>",
            "</script>"
    })
    int batchDelete(@Param("msgIds") List<Long> msgIds);

    int batchInsert(@Param("settings") List<MsgSetting> settings);


    List<MsgGridDto> queryGrid(MsgExample example);


}
