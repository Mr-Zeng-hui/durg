package com.example.demo.interceptor;

import com.example.demo.Model.User;
import com.example.demo.util.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录拦截器
public class LoginInterceptor implements HandlerInterceptor {

    // 请求到达Controller前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.print("请求到达Controller前\t");
//        Object token = request.getAttribute("token");
//
//        if (token == null){
//            response.sendRedirect("/admin");
//            return false;
//        }
//        User user = (User)CacheUtil.get(token.toString());
//        if (StringUtils.isBlank(user.getUserName())){
//            response.sendRedirect("/admin");
//            return false;
//        }
//        if (request.getSession().getAttribute("user") == null){
//            response.sendRedirect("/admin");
//            return false;
//        }
        return true;
    }


    // 跳转JSP前执行，此时可以向Request域添加数据
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.print("跳转JSP前\t");

    }



    // 跳转JSP后执行，此时已经不能向Request域添加数据
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("跳转到JSP后");

    }

}
