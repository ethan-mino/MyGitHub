package org.boostcourse.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogInterceptor extends HandlerInterceptorAdapter {
    @Override   // preHandle()메서드는 컨트롤러의 메서드가 실행되기 전에 실행됨.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(handler.toString() + "를 호출했습니다.");
        return super.preHandle(request, response, handler);
    }

    @Override   // preHandle()메서드는 컨트롤러의 메서드가 실행된 후에 실행됨.
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println(handler.toString() + "가 종료되었습니다. " + modelAndView.getViewName() + "을 view로 사용합니다.");
        super.postHandle(request, response, handler, modelAndView);
    }
}
