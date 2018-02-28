/**
 * @Description: TODO
 * @date 2017��10��29�� ����2:28:02 	
 */
package com.pay.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pay.util.CheckNotify;
import com.pay.util.PropUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
@Controller
public class PayController {
	//����֧������
	@RequestMapping("/pay/CodePay")
	public void CodePay(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String price=request.getParameter("price"); //���ύ�ļ۸�
		String type=request.getParameter("type"); //֧������  1��֧���� 2��QQǮ�� 3��΢��
		String pay_id=request.getParameter("pay_id"); //֧���˵�Ψһ��ʶ
		String param=request.getParameter("param"); //�Զ���һЩ���� ֧���󷵻�
		String notify_url="http://lzclzc.tunnel.qydev.com/mazhifu/pay/notifyJS.action";//֧���ɹ���ҵ��Ĵ���
		String return_url="http://lzclzc.tunnel.qydev.com/mazhifu/iframe.jsp";//֧���ɹ�����ת������
		if(price==null){ 
			price="1";
		}else if(type==null){
			type="1";
		}
		String token = PropUtil.getProp("codepay.token"); //�ǵø��� http://codepay.fateqq.com ��̨������
		String codepay_id =PropUtil.getProp("codepay.codepay_id");//�ǵø��� http://codepay.fateqq.com ��̨�ɻ��
		//��������������ҪURL����
		String url="http://codepay.fateqq.com:52888/creat_order?id="+codepay_id+"&pay_id="+pay_id+"&price="+price+"&type="+type+"&token="+token+"&param="+param+"&notify_url="+notify_url+"&return_url="+return_url;
		response.sendRedirect(url);
	}
	
	//֧���ɹ��󣬻ص�������ҵ���߼�notify_url��
	//����ҵ��
	@RequestMapping("/pay/notifyJS")
	public void JS(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(CheckNotify.check(request, response)){
			//��֤֪ͨ��ͨ���󣬼��ɴ���ҵ��
			System.out.println("ͨ��,ҵ��+1");
		}else{
			System.out.println("��ͨ��,ҵ��-1");
		}
	}

}
