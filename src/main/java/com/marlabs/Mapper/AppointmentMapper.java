package com.marlabs.Mapper;

import com.marlabs.Model.Appointment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppointmentMapper extends BaseMapper<Appointment>{
    int deleteByPrimaryKey(String appId);

    int insert(Appointment record);

    int insertSelective(Appointment record);

    Appointment selectByPrimaryKey(String appId);

    int updateByPrimaryKeySelective(Appointment record);

    int updateByPrimaryKey(Appointment record);

    List<Appointment> queryList(Map<String, Object> param);
}