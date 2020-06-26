package com.marlabs.Util;

/**
 *
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

/**
 * 集合排序
 *
 * @author shamee-loop
 *
 */
public class SortUtils {

    /**
     * 按英文首字母排序
     *
     * @param arrayToSort
     */
    private static String sortStringArray(List<String> arrayList) {

        System.out.println("字符型数组排序,排序前:");
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.print(arrayList.get(i) + ",");
        }
        System.out.println();
        System.out.println("排序后:");

        // 调用数组的静态排序方法sort,且不区分大小写
        // Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(arrayList);

        for (int i = 0; i < arrayList.size(); i++) {
            System.out.print(arrayList.get(i) + ",");
            result += arrayList.get(i);
        }
        System.out.println();
        System.out.println("组装接口签名数据：" + result);
        return result;// 组装接口签名用数据
    }

    /**
     * 参数封装，获取排序数据
     *
     * @param map
     * @return
     */
    public static String sortMap(Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            List<String> list = new ArrayList<String>();
            for (Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getValue());
                if (entry.getValue() != null
                        && !StringUtils.isBlank(entry.getValue().toString())
                        && !entry.getValue().trim().equals("null")) {
                    list.add(entry.getKey() + entry.getValue());
                }
            }
            return sortStringArray(list);
        }
        return null;
//		String mapSign = "";
//		for (Entry<String, String> entry : map.entrySet()) {
//
//		    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//		    if(!StringUtils.isBlank(entry.getValue())){
//		    	mapSign += entry.getKey() + entry.getValue();
//		    }
//		}
//		return mapSign;
    }

    public static void main(String[] args) {
        // String[] arrayToSort = new String[] { "Oscar", "Charlie", "Ryan",
        // "Adam", "David", "aff", "Aff" };
        // sortStringArray(arrayToSort);

        Map<String, String> param = new HashMap<String, String>();
        param.put("Oscar", "11");
        param.put("OscarId", "22");
        param.put("Charlie", "www");
        param.put("David", "qwe12");
        String str = sortMap(param);
        System.out.println(str);
    }

}
