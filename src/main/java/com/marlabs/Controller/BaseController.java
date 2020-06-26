package com.marlabs.Controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marlabs.Model.User;
import com.marlabs.Redis.IUserDao;
import com.marlabs.Util.Constants;
import com.marlabs.Util.MD5;
import com.marlabs.Util.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;


public class BaseController {


    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Value("${interface.type}")
    private String interfaceType;

    @Value("${interface.type}")
    private String memcacheType;

//    public final static String interfaceType = PropertiesUtil.getValue("global.properties", "interface.type");
//    public final static String memcacheType = PropertiesUtil.getValue("global.properties", "memcache.type");
//    public final static String baseProUrl = "http://101.37.24.213:10110/mszs-open";


    //不能包含a，b，a,b用于订单号分隔符，做后台通知回传数据
    private static final String[] randomStr = new String[] {"C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };

    /** 投递记录消耗积分  **/
//    @Value("${delivery.consume.integral}")
//    public Long point;

    /** 重复投递简历间隔时间（单位：S）  **/
//    @Value("${delivery.repeat.time}")
//    public Long repeatTime;



    /**
     * 文件上传 返回路径
     *
     * @param imgFile
     *            文件
     * @param request
     * @param middlePath
     *            文件在upload下的中间路径 如 adv （adv/adv）
     * @return
     * @exception .
     */
    public String uploadFile(MultipartFile imgFile, HttpServletRequest request,
                             String middlePath) {

        String path = null;
        try {
            logger.info("文件上传");
            if (imgFile != null && !imgFile.isEmpty()) {
                logger.info("广告图片不为空");

                // 绝对路径
                String realPath = request.getSession().getServletContext()
                        .getRealPath("/upload");
                // 相对路径
                path = request.getContextPath() + File.separator + "upload";
                // 组装路径
                realPath += File.separator
                        + middlePath
                        + File.separator
                        + new Date().getTime()
                        + imgFile.getOriginalFilename().substring(
                        imgFile.getOriginalFilename().indexOf("."));
                path += File.separator
                        + middlePath
                        + File.separator
                        + new Date().getTime()
                        + imgFile.getOriginalFilename().substring(
                        imgFile.getOriginalFilename().indexOf("."));
                // 文件复制
                FileUtils.copyInputStreamToFile(imgFile.getInputStream(),
                        new File(realPath));
            }
        } catch (Exception e) {
            logger.error("图片上传异常" + e.getMessage(), e);
        }

        return path;
    }

    /**
     *
     * 界面写回字符串信息
     *
     * @param response
     * @param msg
     * @exception .
     */
    public void out(HttpServletResponse response, String msg) {
        PrintWriter out = null;
        try {
            logger.info("写信息到界面");
            out = response.getWriter();
            out.write(msg);
            out.flush();

        } catch (IOException e) {
            logger.info("写信息到界面异常" + e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    /**
     * 检验必传参数是否合法 目前制作String Float Integer类型判断
     *
     * @param objs
     * @throws IOException
     * @throws JSONException
     */
    protected boolean validateData(Object[] objs) throws JSONException, IOException {

        if (objs != null) {
            // 循环属性值进行验证
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] == null) {
                    return false;
                }
                // 如果属性是String 类型
                if (objs[i] instanceof String) {
                    if (objs[i] == null || objs[i].toString().trim().equals("")) {
                        return false;
                    }
                }
                // 属性是Integer或Float类型
                if (objs[i] instanceof Integer || objs[i] instanceof Float) {
                    if (objs[i] == null) {
                        return false;
                    }
                }

            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 默认处理分页索引
     * @param pageNo
     * @return
     */
    public Integer dealPageNo(Integer pageNo){
        if(pageNo != null){
            return pageNo;
        }
        return 1;
    }

    /**
     * 默认处理分页，每页10条数据
     * @param pageSize
     * @return
     */
    public Integer dealPageSize(Integer pageSize){
        if(pageSize != null){
            return pageSize;
        }
        return 10;
    }

    /**
     * 参数为空封装返回
     */
    public String errorParamNull(String paramName){
        return "参数["+paramName+"]为空";
    }



    /**
     *
     * 组装返回的json数据
     *
     * @param data
     * @param code
     * @param message
     * @return
     * @exception .
     */
    public JSONObject assemblyJson(Object data, String code, String message) {

        // 创建对象
        JSONObject jsonObject = new JSONObject();

        // 数据不为空的时候设置返回数据
        jsonObject.put("data", data);

        // 设置状态数据
        jsonObject.put("message", message);
        jsonObject.put("code", code);

        return jsonObject;

    }

    /**
     * 校验接口签名
     * @param paramStr
     * @param sign
     * @return
     */
    public Boolean validateSign(String paramStr, String sign){

        if(!StringUtils.isBlank(interfaceType) && interfaceType.trim().equals("test")){
            return true;
        }

        logger.info("接口传递签名sign:{}", sign);

        logger.info("接口参数加密前:{}", paramStr);
        try {

		    String currSign = MD5.compile(paramStr);
//            String currSign = MD5Utils.md5(paramStr, "utf-8");
            logger.info("接口参数加密后:{}", currSign);

            if(StringUtils.isBlank(sign)){
                return false;
            }

            if(!StringUtils.isBlank(sign) && currSign.trim().equals(sign.trim())){//sign验证合法
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 签名校验失败返回错误信息
     * @return
     */
    public JSONObject respSignError(){
        return assemblyJson(null, Constants.RESP_SIGN_ERROR, "签名校验失败");
    }
    /**
     * 参数不完整返回错误信息
     * @return
     */
    public JSONObject respParamError(){
        return assemblyJson(null, Constants.RESP_PARAM_NULL_ERROR, Constants.RESP_PARAM_NULL_MSG);
    }

    /**
     * http请求
     * @param postUrl
     * @param postHeaders
     * @param postEntity
     * @return
     * @throws IOException
     */
    public String httpPost(String postUrl, Map<String, String> postHeaders, String postEntity) throws IOException {

        URL postURL = new URL(postUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) postURL.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setInstanceFollowRedirects(true);
        httpURLConnection.setRequestProperty(" Content-Type ", " application/x-www-form-urlencoded ");

        if (postHeaders != null) {
            for (String pKey : postHeaders.keySet()) {
                httpURLConnection.setRequestProperty(pKey, postHeaders.get(pKey));
            }
        }
        if (postEntity != null) {
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(postEntity);
            out.flush();
            out.close(); // flush and close
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder sbStr = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sbStr.append(line);
        }
        bufferedReader.close();
        httpURLConnection.disconnect();
        return new String(sbStr.toString().getBytes(), "utf-8");
    }

    public String setAdmCode(String admCode){
        if (admCode != null && admCode.length() ==6) {
            String code = admCode.substring(0, 2);
            String c =  admCode.substring(0, 4);
            admCode = code+"0000" +","+ c +"00" +",100000";
        }

        return admCode;
    }

    /**
     *  消息推送
     * @param fromUserId  推送用户id
     * @param userIds  接受用户id组
     * @param pushType 0群推 1个推
     * @param title 标题
     * @param alert 通知
     * @param apiJson  服务端接口需要参数
     * @param androidJson 安卓内部参数
     * @param iosJson  ios内部参数
     * @param channelType 0企业交流 1岗位交流 2企业任务采纳 3岗位申请
     * @param pushChannelService
     * @param userAppLogService
     * @param pushInfoService
     * @param pushInfoUserService
     */
//	@SuppressWarnings("unchecked")
//	public void push(Integer fromUserId, String[] userIds, String pushType, String title, String alert, JSONObject apiJson,
//			JSONObject androidJson, JSONObject iosJson,
//			String channelType, IPushChannelService pushChannelService,
//			IUserAppLogService userAppLogService, IPushInfoService pushInfoService,
//			IPushInfoUserService pushInfoUserService){
//
//		/**
//		 * 向用户发起消息推送
//		 * 获取toUser 最后登录设备是ios还是android
//		 */
//		Map<String, Object> param = new HashMap<String, Object>();
//		PushInfo pushInfo = new PushInfo();
//		pushInfo.setTitle(title);
//		pushInfo.setContent(alert);
//		pushInfo.setPushType(pushType);
//
//		/**
//		 * 根据channelType获取配置的跳转app包名类名
//		 */
//		param.put("channelType", "0");//企业交流
//		List<PushChannel> channelList = pushChannelService.queryByChannelType(param);
//		if(channelList == null || channelList.size() == 0 || channelList.get(0) == null){
//			pushInfo.setPushStatus("-1");
//			pushInfo.setPushResult("该类型暂未配置app包名类名");
//			pushInfoService.insert(pushInfo);
//			return ;
//		}
//
//		if(StringUtils.isBlank(pushType) || (!pushType.trim().equals("0") && !pushType.trim().equals("1"))){
//			pushInfo.setPushStatus("-1");
//			pushInfo.setPushResult("推送类型有误");
//			pushInfoService.insert(pushInfo);
//			return ;
//		}
//
//		/**
//		 * 设置额外参数，从配置表中获取
//		 */
//		PushChannel pushChannel = channelList.get(0);
//		Map<String, String> extraMap = new HashMap<String, String>();
//		extraMap.put("extraType", pushChannel.getExtraType());
//		extraMap.put("androidPck", pushChannel.getAndroidPck());
//		extraMap.put("androidClass", pushChannel.getAndroidClass());
//		extraMap.put("schemeName", pushChannel.getSchemeName());
//		extraMap.put("schemeUrl", pushChannel.getSchemeUrl());
//		extraMap.put("channelName", pushChannel.getChannelName());
//		extraMap.put("wapUrl", pushChannel.getWapUrl() != null?pushChannel.getWapUrl().toString():null);
//		extraMap.put("isNeedTableBar", pushChannel.getIsNeedTableBar()); //是否需要底部bar，0不需要 1需要
//		extraMap.put("androidJson", androidJson.toJSONString());
//		extraMap.put("iosJson", iosJson.toJSONString());
//		extraMap.put("apiJson", apiJson.toString());
//
//		pushInfo.setExtraType(pushChannel.getExtraType());
//		pushInfo.setAndroidPck(pushChannel.getAndroidPck());
//		pushInfo.setAndroidClass(pushChannel.getAndroidClass());
//		pushInfo.setSchemeName(pushChannel.getSchemeName());
//		pushInfo.setSchemeUrl(pushChannel.getSchemeUrl());
//		pushInfo.setType(channelType);
//		pushInfo.setPushType(pushType);
//		pushInfo.setWapUrl(pushChannel.getWapUrl() != null?pushChannel.getWapUrl().toString():null);
//		pushInfo.setApiJson(apiJson.toString());
//		pushInfo.setIosJson(iosJson.toString());
//		pushInfo.setAndroidJson(androidJson.toString());
//		pushInfo.setIsNeedTablebar(pushChannel.getIsNeedTableBar());
//
//		if(pushType.trim().equals("0")){  //群推
//			JSONObject json = JPushClientUtil.sendAllPlatform(title, alert, extraMap);
//			if(json != null){
//				pushInfo.setPushStatus(json.getString("code"));
//				pushInfo.setPushInfo(json.getString("info"));
//				pushInfo.setPushResult(json.getString("result"));
//
//				pushInfoService.insert(pushInfo);
//				return ;
//			}
//		} else {  //个推
//			if(userIds == null || userIds.length == 0){
//				pushInfo.setPushStatus("-1");
//				pushInfo.setPushResult("未置顶推送用户信息");
//				pushInfoService.insert(pushInfo);
//				return ;
//			}
//
//			for(int i = 0;i<userIds.length; i++){
//				String userId = userIds[i];
//				param.put("userId", userId);
//				List<UserAppLog> appList = userAppLogService.queryList(param);
//				if(appList != null && appList.size() > 0 && appList.get(0) != null){
//					UserAppLog userAppLog = appList.get(0);
//					/**
//					 * 判断deviceToken长度，大于16位为ios，小于16位 为安卓设备
//					 */
//					JSONObject json = null;
//					if(userAppLog != null && !StringUtils.isBlank(userAppLog.getDeviceToken())
//							&& userAppLog.getDeviceToken().length()<=16){
//						json = JPushClientUtil.sendAndroidWithAlias(new String[]{userId}, alert, title, extraMap);
//					} else if(userAppLog != null && !StringUtils.isBlank(userAppLog.getDeviceToken())
//							&& userAppLog.getDeviceToken().length()>16){
//						json = JPushClientUtil.sendIOSWithAlias(new String[]{userId}, alert, title, extraMap);
//					}
//					if(json != null){
//						pushInfo.setPushStatus(json.getString("code"));
//						pushInfo.setPushInfo(json.getString("info"));
//						pushInfo.setPushResult(json.getString("result"));
//
//						int pushInfoId = pushInfoService.insert(pushInfo);
//
//						/**
//						 * 插入pushInfoUser
//						 */
//						PushInfoUser pushInfoUser = new PushInfoUser();
//						pushInfoUser.setPushId(Long.parseLong(pushInfoId + ""));
//						pushInfoUser.setCreateTime(new Date());
//						pushInfoUser.setFromUserId(fromUserId);
//					    pushInfoUser.setToUserId(Integer.parseInt(userId));
//					    pushInfoUser.setIsRead("0");
//					    pushInfoUserService.insert(pushInfoUser);
//					}
//				}
//			}
//		}
//	}
//

    /**
     * 随机生成订单号
     * @return
     */
    public static String generalOrderNo() {
        String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int i = (int)(Math.random()*24);

        //随机数5位置
        int r = (int)(Math.random()*1000);
        return randomStr[i] + str + r;
    }

//	public  UserIntegral getIntegral(ClientParamBo Bo){
//		UserIntegral userIntegral=null;
//		/*User user=null;
//		JSONObject json = FrontSessionManager.getUser(Bo.getToken());
//		if(json != null && !StringUtils.isBlank(json.getString("code")) && json.getString("code").trim().equals(Constants.RESP_SUCCESS)){ //OK
//			user = (User) json.get("data");
//			userIntegral=userIntegralService.selectByPrimaryKey(Long.valueOf(user.getUserId()));
//		} */
//		Long id =(long) 6144;
//		userIntegral=userIntegralService.selectByPrimaryKey(id);
//		return userIntegral;
//	}
    /**
     * 根据后移(前移)分钟数和输入时间计算偏移后的时间
     * @param curDate String
     * @param spanMinute int
     * @return String
     */
    public static String getDateBySpanMinute ( String curDate , int spanMinute ,String pattern)
            throws ParseException
    {
        DateFormat formatter = new SimpleDateFormat(pattern);
        Date d = (Date) formatter.parse(curDate);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add ( Calendar.MINUTE , spanMinute ) ;
        return DateFormatUtils.format(calendar.getTime(), pattern);
    }

    /**
     * 消息模板关键字替换
     * @param template  消息内容
     * @param map 替换key-value
     * @return
     */
    public static String replaceTemplate(String template, Map<String, Object> map){
        if(!StringUtils.isBlank(template)){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                template = template.replaceAll(entry.getKey(), entry.getValue().toString());
            }
        }
        return template;
    }
    public String getAddr(String province,String city,String area){
        String addr =null;
        if(province ==  null || province.length() ==0){
            return "无";
        }
        if(province.indexOf("全部")!= -1){
            addr = "所有地区";
            return addr;
        }
        if(city.indexOf("全部")!= -1){
            addr = province;
            return addr;
        }
        if(area.indexOf("全部")!= -1){
            addr = city;
        }else{
            addr = area;
        }
        return addr;
    }
    public static String genRadomNbr() {
        // TODO Auto-generated method stub
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
    public JSONObject userValidToken(String token, IUserDao userDao){
        JSONObject jsonObject = new JSONObject();
        User user = null;
        try {
            if (!TokenUtils.isValid(token)) {
                jsonObject.put("data", null);
                jsonObject.put("message", "系统登陆超时，请重新登陆。");
                jsonObject.put("code", Constants.RESP_INVLID_TOKEN);
                System.out.println("系统登陆超时，请重新登陆");
                return jsonObject;
            }
            user = userDao.get(token);
            jsonObject.put("data", user);
            jsonObject.put("message", "OK");
            jsonObject.put("code", Constants.RESP_SUCCESS);
            System.out.println("token --ok" + jsonObject);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("data", null);
            jsonObject.put("message", "系统异常");
            jsonObject.put("code", Constants.RESP_SYS_ERROR);
            System.out.println("系统异常");
            return jsonObject;
        }
    }

    public JSONObject compValidToken(String token, ICustomerDao customerDao){
        JSONObject jsonObject = new JSONObject();
        Customer user = null;
        try {
            if (!TokenUtils.isValid(token)) {
                jsonObject.put("data", null);
                jsonObject.put("message", "系统登陆超时，请重新登陆。");
                jsonObject.put("code", Constants.RESP_INVLID_TOKEN);
                return jsonObject;
            }
            user = customerDao.get(token);
            jsonObject.put("data", user);
            jsonObject.put("message", "OK");
            jsonObject.put("code", Constants.RESP_SUCCESS);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("data", null);
            jsonObject.put("message", "系统异常");
            jsonObject.put("code", Constants.RESP_SYS_ERROR);
            return jsonObject;
        }
    }
    /**
     * @author jerry.chen
     * @param brithday
     * @return
     * @throws ParseException
     *             根据生日获取年龄;
     */
    public static int getCurrentAgeByBirthdate(Date brithday)
            throws ParseException, Exception {
        try {Calendar cal = Calendar.getInstance();

            if (cal.before(brithday)) {
                return 0;
            }
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(brithday);

            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

            int age = yearNow - yearBirth;

            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) age--;
                }else{
                    age--;
                }
            }
            System.out.println("age:"+age);
            return age;} catch (Exception e) {
            return 0;
        }
    }
//	private boolean isExistNo(int no,IBaseService<T> iBaseService){
//		Map<String, Object> param = new HashMap<String, Object>();
//		//校验该丁当号是否已被注册过
//		param.put("id", no);
//			List<T> list = iBaseService.queryList(param);
//			logger.info("根据ID号{}获取到用户信息list.size={}", no, list != null ? list.size() : 0);
//			if(list != null && list.size() > 0){
//				logger.info("该ID号已注册");
//				return true;
//			} else {
//				logger.info("该ID号未注册");
//				return false;
//			}
//	}
//
//	public int genNo(IBaseService<T> iBaseService){
//		Random rand = new Random();
//		int no =  10000000 + rand.nextInt(89999999);
//		while(isExistNo(no,iBaseService)){//存在，重新生成no
//			Random rand1 = new Random();
//			no =  10000000 + rand1.nextInt(89999999);
//		}
//		return no;
//	}
}

