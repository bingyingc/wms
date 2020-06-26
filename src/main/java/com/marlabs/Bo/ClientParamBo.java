package com.marlabs.Bo;

/**
 *
 */

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 请求接口必传参数
 *
 * @author shamee-loop
 *
 */
public class ClientParamBo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3973036175924188424L;
    private String token;// 设备token
    private String timestamp;// 请求接口时间戳

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    /**
     * 2016-07-26
     * 新增版本号，兼容旧版本，进行版本号判断。
     * @return
     */
    public String getSign() {

        String result = null;

            result = "timestamp" + getTimestamp();

        if(!StringUtils.isBlank(getToken())){
            result += "token" + getToken();
        }
        return result;

    }

    @Override
    public String toString() {
        return "ClientParamBo [token=" + token + ", timestamp=" + timestamp + "]";
    }

}
