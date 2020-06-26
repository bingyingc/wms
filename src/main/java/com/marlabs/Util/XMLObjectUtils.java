package com.marlabs.Util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


/**
 * XML转Object V1.0  支持List、Set、Object类型<br>
 * XML定义节点名称与Object属性名称要保持一致，不区分大小写。
 * <br>
 * @User： AndyLee
 * @Time：2018年9月18日下午8:48:49
 *
 */
public class XMLObjectUtils {

    /**
     * 方法名称: xmlMappedObj<br>
     * 描述：xml 自动映射成对对象Object ：
     * 	属性支持：
     * 	基本类型、String、Set、List、Object 等
     * 作者: AndyLee
     * 修改日期：2018年9月18日下午8:48:49
     * @param className  类名全路径
     * @param xmlStr
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> Object xmlToObj(String className ,String strXml){
        Object obj = null;
        try {
            obj = getObject(className);
            Class clzz = getInstance(className);
            Element element = getElement(strXml);
            xmlToObj(className, element, obj, clzz);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj ;
    }

    /**
     * 方法名称: xmlMappedObj<br>
     * 描述：xml 自动映射成对对象Object ：
     * 	属性支持：
     * 	基本类型、String、Set、List、Object 等
     * 作者: AndyLee
     * 修改日期：2018年9月18日下午8:48:49
     * @param clz  类名.class
     * @param strXml
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T> Object xmlToObj(Class clz ,String strXml){
        Object obj = null ;
        try {
            String className = clz.getName();
            obj = getObject(className);
            Class clzz = getInstance(className);
            Element element = getElement(strXml);
            xmlToObj(className, element, obj, clzz);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj ;
    }

    public static <T> Object toXML1(String className ,String respXML){
        Object obj = getObject(className);
        Class clzz = getInstance(className);
        Element element = getElement(respXML);
        for (Field field : getInstance(className).getDeclaredFields()) {
            Iterator iterator = element.elementIterator();
            while(iterator.hasNext()){
                Element el = (Element) iterator.next();
                if(field.getName().equals(el.getName())){
                    field = getField(clzz, el.getName());
                    field.setAccessible(true);
                    try {
                        field.set(obj, el.getText());
                    } catch (IllegalArgumentException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        return obj ;
    }

    public static Element getElement(String xml){
        Element element = null ;
        Document doc = null ;
        try {
            doc = DocumentHelper.parseText(xml);
            element = doc.getRootElement() ;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return element ;
    }

	/*public static String objToJosn(Object obj){
		if(obj != null){
			return JSONObject.fromObject(obj).toString();
		}
		return "" ;
	}*/

    /**
     * Object - 》 XML 属性只支持String类转换 <br>
     * @param obj 对象
     * @return <br>
     */
    @SuppressWarnings("rawtypes")
    public static String froXML(Object obj){
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        Class clzz = obj.getClass();
        for (Field f : clzz.getDeclaredFields()){
            if(f.getGenericType().toString().equals("class java.lang.String")){
                Method m =  getMethod(clzz, "get" + getMethodName(f.getName()));
                String value = getValue(m, obj);
                if(value != null){
                    packageParams.put(f.getName(),  value);
                }
            }
        }
        return objToXML(packageParams) ;
    }


    @SuppressWarnings("rawtypes")
    public static String getSign(Object obj){
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();
        Class clzz = obj.getClass();
        for (Field f : clzz.getDeclaredFields()){
            if(f.getGenericType().toString().equals("class java.lang.String")){
                Method m =  getMethod(clzz, "get" + getMethodName(f.getName()));
                String value = getValue(m, obj);
                if(value != null){
                    packageParams.put(f.getName(),  value);
                }

            }
        }
        return objToXML(packageParams) ;
    }

    private static String getValue(Method method ,Object obj){
        String value = null ;
        try {
            value = (String) method.invoke(obj);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value ;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Method getMethod(Class clzz ,String name){
        Method method = null ;
        try {
            method = clzz.getMethod(name);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return method ;
    }

    private static String getMethodName(String fildeName){
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    @SuppressWarnings("rawtypes")
    private static Field getField(Class clzz ,String labelName){
        Field field = null ;
        try {
            field = clzz.getDeclaredField(labelName);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return field ;
    }

	/*private static JSONObject getJson(String json){
		JSONObject jsonObject = null ;
		try {
			jsonObject = JSONObject.fromObject(json) ;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return jsonObject ;
	}*/

    private static Object getObject(String className){
        Object obj = null ;
        try {
            obj = Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj ;
    }


    private static void analysisAttribute(Class clzz, Element element, Object obj, Class zl){
        for(Field fd : getFields(zl)){
            analysisValue(getIterator(element), element, fd, obj, clzz);
        }
    }

    private static void analysisValue(Iterator iterator, Element element, Field field, Object obj, Class clzz){
        while(iterator.hasNext()){
            Element ele = getElement(iterator);
            String key = ele.getName();
            Object value = ele.getText();
            if(checkAttribute(field, key)){
                checkAttributeType(field, obj, clzz, key, value, ele);
            }
			/*if(checkAttribute(field, key) && checkIsEmpty(value)){
				checkAttributeType(field, obj, clzz, key, value, ele);
			}*/
        }
    }

//	private static void analysisValues(Iterator iterator, JSONObject json, Field field, Object obj, Class clzz){
//		while(iterator.hasNext()){
//			String key = getJSONArray(iterator);
//			Object value = getValue(key, json);
//			if(checkAttribute(field, key)){
//				checkAttributeType(field, obj, clzz, key, value);
//			}
//		}
//	}


    private static void checkAttributeType(Field field, Object obj, Class clzz, String key, Object value, Element element){
        if(checkAttribute(field, key)){
            if(checkListType(field)){
                setTypeValue(field, obj, clzz, key, setListType(field, obj, element));
            }else if(checkSetType(field)){
                setTypeValue(field, obj, clzz, key, setSetType(field, obj, element));
            }else if(checkObject(field)){
                setTypeValue(field, obj, clzz, key, setObjectType(field, element));
            }else{
                setTypeValue(field, obj, clzz, key, value);
            }
        }
    }


    private static List setListType(Field field, Object obj, Element element){
        List list = new ArrayList();
        Iterator it = element.elementIterator();
        Class<?> clz = getClassInstantiation(getParameterizedType(getType(field)));
        Field [] fs = getFields(clz);
        while(it.hasNext()){
            Object oj = getObject(clz.getName());
            Element el =  (Element) it.next();
            for(Field fi : fs){


                if(fi.getName().equals(el.getName())){
                    String key = el.getName();
                    String value = el.getText();
                    setTypeValue(fi, oj, clz, key, value);;
                }
            }
            list.add(oj);
        }
        return list ;
    }

    private static Set setSetType(Field field, Object obj, Element element){
        Set set = new HashSet();
        Class<?> clz = getClassInstantiation(getParameterizedType(getType(field)));
        Iterator it = element.elementIterator();
        Field [] fs = getFields(clz);
        while(it.hasNext()){
            Object oj = getObject(clz.getName());
            Element el =  (Element) it.next();
            for(Field fi : fs){
                if(fi.getName().equals(el.getName())){
                    String key = el.getName();
                    String value = el.getText();
                    setTypeValue(fi, oj, clz, key, value);;
                }
            }
            set.add(oj);
        }
        return set ;
    }


    private static <T> Object setObjectType(Field field, Element element){
        Class clzz = field.getType() ;
        Object oj = getObject(clzz.getName());
        analysisAttribute(clzz, element, oj, clzz);
        return oj ;
    }

    private static Object getValue(String key, Element element){
        return element.getText();
    }

    private static Element getElement(Iterator iterator){
        Element book = (Element) iterator.next();
        return book;
    }

    private static Element getElem(Iterator iterator){
        Element book = (Element) iterator.next();
        return book;
    }

	/*private static JSONObject getKeyJson(Iterator iterator){
		 return (JSONObject) iterator.next();
	}*/

    private static Iterator getIterator(Element element){
        return element.elementIterator();
    }


    private static void xmlToObj(String className, Element element, Object obj, Class clzz){
        analysisAttribute(clzz, element, obj, getInstance(className));
    }

    private static Field[] getFields(Class<?> clzz){
        return clzz.getDeclaredFields();
    }

    private static Class<?> getClassInstantiation(ParameterizedType paraType){
        return (Class<?>)paraType.getActualTypeArguments()[0];
    }

    private static ParameterizedType getParameterizedType(Type type){
        return (ParameterizedType) type;
    }

    private static Type getType(Field field){
        return field.getGenericType();
    }

	/*private static JSONArray getJSONArray(String json){
		return JSONArray.fromObject(json);
	}*/


    public static void setAccessibleFlag(Field field){
        field.setAccessible(true);
    }

    private static void setTypeValue(Field field, Object obj, Class clzz, String key, Object value){
        if(checkAttribute(field, key)){
            try {
                checkType(getDeclaredField(clzz, field.getName()), obj, value);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static boolean checkAttribute(Field field, String attribute){
        boolean flag = false ;
        if(field.getName().toUpperCase().equals(attribute.toUpperCase())){
            flag = true ;
        }
        return flag ;
    }

    private static Field getDeclaredField(Class clz, String key){
        return getField(clz, key);

    }

    private static void checkType(Field field, Object obj, Object value) throws NumberFormatException, IllegalArgumentException, IllegalAccessException{
        setAccessibleFlag(field);
        if(checkInt(field)){
            field.setInt(obj, Integer.parseInt(value.toString()));
        }else if(checkFloat(field)){
            field.setFloat(obj, Float.parseFloat(value.toString()));
        }else if(checkDouble(field)){
            field.setDouble(obj, Double.parseDouble(value.toString()));
        }else if(checkShort(field)){
            field.setShort(obj, Short.parseShort(value.toString()));
        }else if(checkLong(field)){
            field.setLong(obj, Long.parseLong(value.toString()));
        }else if(checkByte(field)){
            field.setByte(obj, Byte.parseByte(value.toString()));
        }else if(checkBoolean(field)){
            field.setBoolean(obj, Boolean.parseBoolean(value.toString()));
        }else{
            field.set(obj, value);
        }
    }

    private static boolean checkInt(Field field){
        boolean flag = false ;
        if(field.getType().equals(int.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkFloat(Field field){
        boolean flag = false ;
        if(field.getType().equals(float.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkDouble(Field field){
        boolean flag = false ;
        if(field.getType().equals(double.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkLong(Field field){
        boolean flag = false ;
        if(field.getType().equals(long.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkChar(Field field){
        boolean flag = false ;
        if(field.getType().equals(char.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkByte(Field field){
        boolean flag = false ;
        if(field.getType().equals(byte.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkShort(Field field){
        boolean flag = false ;
        if(field.getType().equals(short.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkBoolean(Field field){
        boolean flag = false ;
        if(field.getType().equals(boolean.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkListType(Field field){
        boolean flag = false ;
        if(field.getType().equals(java.util.List.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkSetType(Field field){
        boolean flag = false ;
        if(field.getType().equals(java.util.Set.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkDateType(Field field){
        boolean flag = false ;
        if(field.getType().equals(java.util.Date.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkStringType(Field field){
        boolean flag = false ;
        if(field.getType().equals(String.class)){
            flag = true ;
        }
        return flag ;
    }

    private static boolean checkIntegerType(Field field){
        boolean flag = false ;
        if(field.getType().equals(java.lang.Integer.class)){
            flag = true ;
        }
        return flag ;
    }


    private static boolean checkType(Field field){
        boolean flag = false ;
        if(checkInt(field)){
            flag = true ;
        }else if(checkFloat(field)){
            flag = true ;
        }else if(checkDouble(field)){
            flag = true ;
        }else if(checkLong(field)){
            flag = true ;
        }else if(checkChar(field)){
            flag = true ;
        }else if(checkByte(field)){
            flag = true ;
        }else if(checkShort(field)){
            flag = true ;
        }else if(checkBoolean(field)){
            flag = true ;
        }else if(checkDateType(field)){
            flag = true ;
        }else if(checkStringType(field)){
            flag = true ;
        }else if(checkIntegerType(field)){
            flag = true ;
        }
        return flag ;
    }

    public static boolean checkObject(Field field){
        boolean flag = false ;
        if(!field.getType().toString().startsWith("java.") && !checkType(field)){
            flag = true ;
        }
        return flag ;
    }


	/*@SuppressWarnings("rawtypes")
	private static void jsonListObj(String className, JSONObject jsonObject, Object obj, Class clzz){
		for (Field field : getInstance(className).getDeclaredFields()) {
			Iterator iterator = jsonObject.keys() ;
			while(iterator.hasNext()){
				String key = (String) iterator.next();
                String value = jsonObject.getString(key);
				if(field.getName().toUpperCase().equals(key.toUpperCase())){
					field = getField(clzz, key);
					field.setAccessible(true);
					try {
						field.set(obj, value);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}*/


    @SuppressWarnings("rawtypes")
    private static Class getInstance(String className){
        Class clzz = null ;
        try {
            clzz = Class.forName(className).newInstance().getClass();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return clzz ;
    }

    private static boolean checkIsEmpty(Object obj){
        boolean flag = false ;
        if(obj != null && !"null".equals(obj.toString()) && !"[]".equals(obj.toString())){
            if (!"".equals(obj.toString().trim())) {
                flag = true ;
            }
        }
        return flag ;
    }

    @SuppressWarnings("rawtypes")
    private static String objToXML(SortedMap<Object,Object> parameters) {
        StringBuilder sb2 = new StringBuilder();
        try {
            sb2.append("<?xml version='1.0' encoding='UTF-8' ?>"+"\n"+"<Content>");
            sb2.append("\n");
            Set es = parameters.entrySet();
            Iterator it = es.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry)it.next();
                String k = (String)entry.getKey();
                Object v = entry.getValue();
                sb2.append("<" + initialUpper(k) + ">");
                if("messageType".equals(k)){
                    sb2.append(v.equals("text") ? "1" : "2");
                }else{
                    sb2.append(v);
                }
                sb2.append("</" + initialUpper(k) + ">");
                sb2.append("\n");
            }
            sb2.append("</Content>");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb2.toString() ;
    }

    /**
     * GBK转UTF-8<br>
     * @param gbk
     * @return <br>
     */
    public static String gbkToUtf8(String gbk){
        String utf8 = null ;
        try {
            utf8 = new String(new String(gbk.getBytes("UTF-8"),"ISO-8859-1").getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return utf8 ;
    }

    public static String initialUpper(String convertString){

        return convertString.substring(0, 1).toUpperCase()+convertString.substring(1, convertString.length());
    }

    public static String initialLower(String convertString){

        return convertString.substring(0, 1).toLowerCase()+convertString.substring(1, convertString.length());
    }


}
