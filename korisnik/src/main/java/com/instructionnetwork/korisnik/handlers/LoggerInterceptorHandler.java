package com.instructionnetwork.korisnik.handlers;

import com.instructionnetwork.korisnik.services.GRPCClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggerInterceptorHandler implements HandlerInterceptor {

    @Autowired
    private GRPCClientService grpcClientService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        grpcClientService.save("Korisnik", request.getMethod(), request.getRequestURI(), HttpStatus.valueOf(response.getStatus()).toString());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
