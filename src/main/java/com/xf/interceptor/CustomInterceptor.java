package com.xf.interceptor;

import com.xf.service.KfcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomInterceptor implements HandlerInterceptor {

    private KfcService kfcService;
    @Autowired
    public void KfcService(KfcService kfcService){
        this.kfcService = null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (kfcService == null) {
            response.getWriter().write("kfcService is null");
            return false;
        }
        return true;
    }
}