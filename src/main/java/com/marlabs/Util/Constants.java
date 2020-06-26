package com.marlabs.Util;

import java.util.HashMap;
import java.util.Map;

/**
 *   S，system level error
 *   P，param level error
 *   B，business level error
 *
 */
@SuppressWarnings("serial")
public class Constants {

    public static final String INVLID_TOKEN_TIPS = "Token invalid, please login again";

    public static final String RESP_SUCCESS = "0";

    public static final String RESP_SYS_ERROR = "S0001";

    public static final String RESP_PARAM_NULL_ERROR = "P0001";

    public static final String RESP_PARAM_ERROR = "P0002";

    public static final String RESP_CLIENT_PARAM_ERROR = "P0003";

    public static final String RESP_TIMESTAMP_ERROR = "P0004";

    public static final String RESP_SIGN_ERROR = "P0005";

    public static final String RESP_BUSS_ERROR = "B0001";

    public static final String RESP_DATA_NULL_ERROR = "B0002";

    public static final String RESP_INVLID_TOKEN = "D0005";

    public static final Integer CALL_TIME_HOUR = 1; //Default 1 hour

    public static final String RESP_PARAM_NULL_MSG = "Incomplete parameter";

    public static final String PHONE_PATTERN = "^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";

    public static final String PWD_MD5_KEY = "W&9704*26#^%";


    public static final Map<String, Object> clientParamMap = new HashMap<String, Object>(){{
        put("client_type", "client_type");
        put("imei", "imei");
//		put("token", "token");
        put("os_version", "os_version");
        put("timestamp", "timestamp");
    }};

}
