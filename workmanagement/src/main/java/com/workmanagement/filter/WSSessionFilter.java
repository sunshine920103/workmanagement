package com.workmanagement.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.workmanagement.util.Constants;

public class WSSessionFilter implements Filter {
	
	static List<String> checkPath = new ArrayList<String>();
	
	static {
		//商铺端
		checkPath.add("/ws/merchant/order/list.jhtml");
		checkPath.add("/ws/merchant/order/send_goods.jhtml");
		checkPath.add("/ws/merchant/order/pick_goods.jhtml");
		
		//用户端
		checkPath.add("/ws/cart/list.jhtml");
		checkPath.add("/ws/cart/add.jhtml");
		checkPath.add("/ws/cart/del.jhtml");
		checkPath.add("/ws/cart/update_quantity.jhtml");
		
		checkPath.add("/ws/user/receive_address.jhtml");
		checkPath.add("/ws/user/del_receive_address.jhtml");
		checkPath.add("/ws/user/set_default_receive_address.jhtml");
		checkPath.add("/ws/user/save_receive_address.jhtml");
		checkPath.add("/ws/user/consume_record.jhtml");
		checkPath.add("/ws/user/get_member_self_pick_up.jhtml");
		checkPath.add("/ws/user/get_messages.jhtml");
		checkPath.add("/ws/user/messages.jhtml");
		checkPath.add("/ws/user/get_mycoupon.jhtml");
		checkPath.add("/ws/user/balance_record.jhtml");	
		checkPath.add("/ws/user/new_member_self_pick_up.jhtml");
		checkPath.add("/ws/user/delete_member_self_pick_up.jhtml");
		
		checkPath.add("/ws/order/confirm_receiving.jhtml");
		checkPath.add("/ws/order/list.jhtml");
		checkPath.add("/ws/order/create_orders.jhtml");
		checkPath.add("/ws/order/del.jhtml");
		checkPath.add("/ws/order/comments.jhtml");
		checkPath.add("/ws/order/get_payment_info.jhtml");
		checkPath.add("/ws/order/purchased_product.jhtml");
		checkPath.add("/ws/order/from_neighbour.jhtml");
	}
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (!checkPath.contains(req.getServletPath())) {
			chain.doFilter(request, response);
		} else {
			Object obj = req.getSession().getAttribute(Constants.SESSION_KEY_MEMBER);
			Object obj1 = req.getSession().getAttribute(Constants.SESSION_KEY_MERCHANT);
			if (obj == null && obj1 == null) {
				res.setHeader("Content-type", "text/html;charset=UTF-8");
				res.setCharacterEncoding("UTF-8");
				res.getWriter().print("{\"status\":\"209\",\"message\":\"请先登录！\"}");
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
