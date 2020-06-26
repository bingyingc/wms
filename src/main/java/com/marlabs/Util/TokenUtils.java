package com.marlabs.Util;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;

/**
 * 生成用户token工具类
 * @author shamee-loop
 *
 */
public class TokenUtils {

    /**
     * 用户token信息有效期，默认2小时
     */
//    private static long tokenValidTime = Long.parseLong(PropertiesUtil.getValue("global.properties", "token.valid.time"));
    @Value("${token.valid.time}")
    private static long tokenValidTime;


    /**
     * base64生成token信息
     * @return
     */
    public static String createToken() {
        String token = UUID.randomUUID().toString();// 随机生成
        // 去除uuid的-
        token = token.substring(0, 8) + token.substring(9, 13)
                + token.substring(14, 18) + token.substring(19, 23)
                + token.substring(24);
        // 加入时间戳
        token = token + "_" + String.valueOf(System.currentTimeMillis());
        token = Base64Utils.encodeToString(token.getBytes());// 用base64加密
        return token;

    }


    /**
     * 判断token有效性
     * @param token
     * @return
     */
    public static boolean isValid(String token) {
        if(StringUtils.isEmpty(token)){
            return false;
        }
        token = new String(Base64Utils.decode(token.getBytes()));// 先解密
        if(token.contains("_")){
            long tokenTime = Long.parseLong(token.split("_")[1]);
            if ((tokenTime - System.currentTimeMillis()) / 1000 > tokenValidTime * 3600) {
                return false;// 已经过期
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    public static void main(String[] args) {
        String token = createToken();
        System.out.println("NGM1ZGMyMDMxYjI3NDg1N2FlODg1MjI0ZTMxZWRkZWNfMTUxMzkxMDI3ODg2Mw==");
        System.out.println(isValid(token));
    }

}
