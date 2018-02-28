/**
 * @Description: TODO
 * @date 2017年10月29日 下午2:28:02 	
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
	//创建支付订单
	@RequestMapping("/pay/CodePay")
	public void CodePay(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String price=request.getParameter("price"); //表单提交的价格
		String type=request.getParameter("type"); //支付类型  1：支付宝 2：QQ钱包 3：微信
		String pay_id=request.getParameter("pay_id"); //支付人的唯一标识
		String param=request.getParameter("param"); //自定义一些参数 支付后返回
		String notify_url="http://lzclzc.tunnel.qydev.com/mazhifu/pay/notifyJS.action";//支付成功后业务的处理
		String return_url="http://lzclzc.tunnel.qydev.com/mazhifu/iframe.jsp";//支付成功后跳转到哪里
		if(price==null){ 
			price="1";
		}else if(type==null){
			type="1";
		}
		String token = PropUtil.getProp("codepay.token"); //记得更改 http://codepay.fateqq.com 后台可设置
		String codepay_id =PropUtil.getProp("codepay.codepay_id");//记得更改 http://codepay.fateqq.com 后台可获得
		//参数有中文则需要URL编码
		String url="http://codepay.fateqq.com:52888/creat_order?id="+codepay_id+"&pay_id="+pay_id+"&price="+price+"&type="+type+"&token="+token+"&param="+param+"&notify_url="+notify_url+"&return_url="+return_url;
		response.sendRedirect(url);
	}
	
	//支付成功后，回掉（处理业务逻辑notify_url）
	//升级业务
	@RequestMapping("/pay/notifyJS")
	public void JS(HttpServletRequest request,HttpServletResponse response) throws Exception{
		if(CheckNotify.check(request, response)){
			//验证通知，通过后，即可处理业务
			System.out.println("通过,业务+1");
		}else{
			System.out.println("不通过,业务-1");
		}
	}

}
