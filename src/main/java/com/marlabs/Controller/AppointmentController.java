package com.marlabs.Controller;

import com.alibaba.fastjson.JSONObject;
import com.marlabs.Model.Appointment;
import com.marlabs.Service.IAppointmentService;
import com.marlabs.Util.Constants;
import com.marlabs.Vo.AppointmentListVo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AppointmentController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IAppointmentService appointmentService;

    @RequestMapping(value = "/appointList", method = {RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public AppointmentListVo appointList(@RequestBody String requestInfoString) {
        List<Appointment> appointmentList =  new ArrayList<>();
        AppointmentListVo appointmentListVo = new AppointmentListVo();
        String requestInfoStringDecode = null;
        Map<String, Object> param = new HashMap<String, Object>();
        int total = 0;
        try {
            requestInfoStringDecode = java.net.URLDecoder.decode(requestInfoString, "UTF-8");
            HashMap<String,Object> requestInfoMap =
                    new ObjectMapper().readValue(requestInfoStringDecode, HashMap.class);
            String action = String.valueOf(requestInfoMap.get("action"));
            Integer pageSize = (Integer) requestInfoMap.get("pageSize");
            Integer pageNumber = (Integer) requestInfoMap.get("pageNumber");
            Integer pageOffset = pageNumber * pageSize;
            param.clear();
            param.put("action",action);
            param.put("pageSize", pageSize);
            param.put("pageOffset",pageOffset);
            appointmentList = appointmentService.queryList(param);
            total = appointmentService.queryByCount(param);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        appointmentListVo.setAppointmentList(appointmentList);
        appointmentListVo.setTotal(total);
        return appointmentListVo;
    }

    @RequestMapping(value = "/appointEdit", method = {RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public JSONObject appointEdit(@RequestBody String requestInfoString) {
        String requestInfoStringDecode = null;
        int result = 0;
        try {
            requestInfoStringDecode = java.net.URLDecoder.decode(requestInfoString, "UTF-8");
            HashMap<String,Object> requestInfoMap =
                    new ObjectMapper().readValue(requestInfoStringDecode, HashMap.class);
            String appIdString = String.valueOf(requestInfoMap.get("appId"));
            String action = String.valueOf(requestInfoMap.get("action"));
            String status = (String) requestInfoMap.get("status");
            Appointment appointment = new Appointment();
            appointment.setAction(action);
            appointment.setStatus(status);
            if(!appIdString.equalsIgnoreCase("null") ){
                Long appId = Long.valueOf(appIdString);
                appointment.setAppId(appId);
                result = appointmentService.updateByPrimaryKeySelective(appointment);
            }else{
                //返回的是插入的customer id
                result = appointmentService.insert(appointment);
            }
            if(result > 0){
//                List<Appointment> customerList = customerService.queryList()
                //no need to query again, because when go back to list page,it's will init again??
                return assemblyJson(result, Constants.RESP_SUCCESS, "修改成功");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assemblyJson(result, Constants.RESP_BUSS_ERROR, "修改失败");
    }


    @RequestMapping(value = "/getAppoint/{code}", method = {RequestMethod.GET})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public JSONObject getAppoint(@PathVariable("code") Long code) {
        Map<String, Object> param = new HashMap<String, Object>();
        Appointment appointment = new Appointment();
        try {
            param.clear();
            param.put("appId", code);
            appointment = appointmentService.queryList(param).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assemblyJson(appointment, Constants.RESP_SUCCESS, "请求成功");
    }
}
