package com.marlabs.Controller;

import com.alibaba.fastjson.JSONObject;
import com.marlabs.Model.Role;
import com.marlabs.Service.IRoleService;
import com.marlabs.Util.Constants;
import com.marlabs.Vo.RoleListVo;
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
public class RoleController extends BaseController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IRoleService roleService;

    @RequestMapping(value = "/roleList", method = {RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public RoleListVo getRoleList(@RequestBody String requestInfoString) {
        List<Role> roleList =  new ArrayList<>();
        RoleListVo roleListVo = new RoleListVo();
        String requestInfoStringDecode = null;
        Map<String, Object> param = new HashMap<String, Object>();
        int total = 0;
        try {
            requestInfoStringDecode = java.net.URLDecoder.decode(requestInfoString, "UTF-8");
            HashMap<String,Object> requestInfoMap =
                    new ObjectMapper().readValue(requestInfoStringDecode, HashMap.class);
            String nameLk = (String) requestInfoMap.get("nameLk");
            Integer pageSize = (Integer) requestInfoMap.get("pageSize");
            Integer pageNumber = (Integer) requestInfoMap.get("pageNumber");
            Integer pageOffset = pageNumber * pageSize;
            param.clear();
            param.put("pageSize", pageSize);
            param.put("roleName", nameLk);
//            param.put("customerId", codeEq);
            param.put("isDelete", "0");
            param.put("pageOffset",pageOffset);
            roleList = roleService.queryList(param);
            total = roleService.queryByCount(param);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        roleListVo.setRoleList(roleList);
        roleListVo.setTotal(total);
        return roleListVo;
    }

    @RequestMapping(value = "/roleEdit", method = {RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public JSONObject editCustomer(@RequestBody String requestInfoString) {
        String requestInfoStringDecode = null;
        int result = 0;
        try {
            requestInfoStringDecode = java.net.URLDecoder.decode(requestInfoString, "UTF-8");
            HashMap<String,Object> requestInfoMap =
                    new ObjectMapper().readValue(requestInfoStringDecode, HashMap.class);

            String roleIdString = String.valueOf(requestInfoMap.get("roleId"));
            String roleName = (String) requestInfoMap.get("roleName");
            String description = (String) requestInfoMap.get("description");
            Boolean isDelete = Boolean.valueOf(String.valueOf(requestInfoMap.get("delete")));
            Role role = new Role();

            role.setRoleName(roleName);
            role.setDescription(description);
            role.setDelete(isDelete);
            if(!roleIdString.equalsIgnoreCase("null") ){
                Long roleId = Long.valueOf(roleIdString);
                role.setRoleId(roleId);
                result = roleService.updateByPrimaryKeySelective(role);
            }else{
                result = roleService.insert(role);
            }

            if(result > 0){
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


    @RequestMapping(value = "/getRole/{code}", method = {RequestMethod.GET})
    @ResponseBody
    @CrossOrigin(origins = "*")
    public JSONObject getCustomer(@PathVariable("code") Long code) {
        Map<String, Object> param = new HashMap<String, Object>();
        Role role = new Role();
        try {
            param.clear();
            param.put("roleId", code);
            role = roleService.queryList(param).get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assemblyJson(role, Constants.RESP_SUCCESS, "请求成功");
    }
}
