package com.example.reservation_platform.mapper;

import com.example.reservation_platform.dto.AppointQueryResultDto;
import com.example.reservation_platform.entity.Tbca003;

import org.apache.ibatis.annotations.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface Tbca003Mapper {

    @Select("SELECT SNO, LIST_NO, LIST_CAMPCODE, RECALL_TIME, REC_TIME, UPDATE_TIME, UPDATE_USER FROM tbca003 WHERE SNO = #{sno}")
    Tbca003 selectBySno(Integer sno);

    @Update("UPDATE tbca003 SET RECALL_TIME = #{recallTime}, UPDATE_TIME = #{updateTime}, UPDATE_USER = #{updateUser} WHERE SNO = #{sno}")
    int updateAppoint(Tbca003 tbca003);

    @Select("SELECT " +
            "  '' AS executionResult, " +
            "  a003.LIST_NO AS listNo, " +
            "  a008.CUST_NAME AS custName, " +
            "  b001.CAMP_NAME AS campName, " +
            "  a003.RECALL_TIME AS recallTime, " +
            "  a001.LIST_LASTPHONE AS listLastPhone, " +
            "  b001.CAMP_SERVICE_DT AS campServiceDt " +
            "FROM tbca003 a003 " +
            "INNER JOIN tbca001 a001 ON a003.LIST_NO = a001.LIST_NO " +
            "INNER JOIN tbca008 a008 ON a001.CUST_NO = a008.CUST_NO " +
            "INNER JOIN tbcb001 b001 ON a003.LIST_CAMPCODE = b001.CAMP_CODE " +
            "WHERE a003.REC_TIME IS NULL " +
            "  AND a003.RECALL_TIME BETWEEN #{startTime} AND #{endTime} " +
            "ORDER BY a003.RECALL_TIME ASC")
    List<AppointQueryResultDto> queryUnfinishedAppointments(
            @Param("startTime") LocalDateTime startTime, 
            @Param("endTime") LocalDateTime endTime
    );
}