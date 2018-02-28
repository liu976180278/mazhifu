/**
 * @Description: TODO
 * @date 2017年12月5日 下午4:03:21 	
 */
package com.pay.util;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lzc
 *
 */
public class CheckNotify {
	//验证通知
	public static boolean check(HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		String key = PropUtil.getProp("codepay.key"); //记得更改 http://codepay.fateqq.com 后台可设置
		Map<String,String> params = new HashMap<String,String>(); //申明hashMap变量储存接收到的参数名用于排序
		Map requestParams = request.getParameterMap(); //获取请求的全部参数
		String valueStr = ""; //申明字符变量 保存接收到的变量
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			valueStr = values[0];
			//乱码解决，这段代码在出现乱码时使用。如果sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);//增加到params保存
			System.out.println("name"+name);
			System.out.println("value"+valueStr);
		}
		List<String> keys = new ArrayList<String>(params.keySet()); //转为数组
	  	Collections.sort(keys); //重新排序
		String prestr = "";
		String sign= params.get("sign"); //获取接收到的sign 参数
		
	        for (int i = 0; i < keys.size(); i++) { //遍历拼接url 拼接成a=1&b=2 进行MD5签名
	            String key_name = keys.get(i);
	            String value = params.get(key_name);
		    	if(value== null || value.equals("") ||key_name.equals("sign")){ //跳过这些 不签名
		    		continue;
		    	}
		    	if (prestr.equals("")){
		    		prestr =  key_name + "=" + value;
		    	}else{
					prestr =  prestr +"&" + key_name + "=" + value;
		    	}
	        }
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update((prestr+key).getBytes());
		String  mySign = new BigInteger(1, md.digest()).toString(16).toLowerCase();
		if(mySign.length()!=32)mySign="0"+mySign;
		if(mySign.equals(sign)){ 
			//编码要匹配 编码不一致中文会导致加密结果不一致
			out.print("ok");
			return true;
		}else{
			//参数不合法
			out.print("fail");
			return false;
		}
	}
}
