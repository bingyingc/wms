package com.marlabs.Service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.marlabs.Mapper.AppointmentMapper;
import com.marlabs.Model.Appointment;
import com.marlabs.Service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

@Service(interfaceClass= IAppointmentService.class, timeout=30000)
@Component
public class AppointmentServiceImpl extends BaseServiceImpl<Appointment> implements IAppointmentService{
    private final static Logger log= Logger.getLogger(IAppointmentService.class);

    @Autowired
    private AppointmentMapper mapper;


    public AppointmentMapper getMapper() {
        return mapper;
    }

    @Override
    public int insert(Appointment record) {
        mapper.insert(record);
        return record.getAppId().intValue();
    }

    @Override
    public List<Appointment> queryList(Map<String, Object> paramMap){
        return mapper.queryList(paramMap);
    }

}
