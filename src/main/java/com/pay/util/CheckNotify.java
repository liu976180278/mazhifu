/**
 * @Description: TODO
 * @date 2017��12��5�� ����4:03:21 	
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
	//��֤֪ͨ
	public static boolean check(HttpServletRequest request,HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		String key = PropUtil.getProp("codepay.key"); //�ǵø��� http://codepay.fateqq.com ��̨������
		Map<String,String> params = new HashMap<String,String>(); //����hashMap����������յ��Ĳ�������������
		Map requestParams = request.getParameterMap(); //��ȡ�����ȫ������
		String valueStr = ""; //�����ַ����� ������յ��ı���
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			valueStr = values[0];
			//����������δ����ڳ�������ʱʹ�á����sign�����Ҳ����ʹ����δ���ת��
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);//���ӵ�params����
			System.out.println("name"+name);
			System.out.println("value"+valueStr);
		}
		List<String> keys = new ArrayList<String>(params.keySet()); //תΪ����
	  	Collections.sort(keys); //��������
		String prestr = "";
		String sign= params.get("sign"); //��ȡ���յ���sign ����
		
	        for (int i = 0; i < keys.size(); i++) { //����ƴ��url ƴ�ӳ�a=1&b=2 ����MD5ǩ��
	            String key_name = keys.get(i);
	            String value = params.get(key_name);
		    	if(value== null || value.equals("") ||key_name.equals("sign")){ //������Щ ��ǩ��
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
			//����Ҫƥ�� ���벻һ�����Ļᵼ�¼��ܽ����һ��
			out.print("ok");
			return true;
		}else{
			//�������Ϸ�
			out.print("fail");
			return false;
		}
	}
}
