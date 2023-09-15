package com.xf.aspect;

import cn.hutool.core.date.DateException;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xf.service.KfcService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class NullCheckAspect {
    private KfcService kfcService;
    @Autowired
    public void KfcService(KfcService kfcService){
        this.kfcService = null;
    }



//    @Before(value = "execution(* com.xf.controller.*.*(..))")
//    public void before(JoinPoint joinPoint) {
//        log.info("检查服务是否为空...");
//        if (kfcService == null) {
//            log.error("kfcService is null");
////            throw new DateException("kfcService is null");
//        }
//    }

//    @Around("execution(* com.xf.controller.*.*(..))")
//    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
//        log.info("环绕通知....");
//
//        if (kfcService == null) {
//            // 如果注入的对象为空，直接返回
//            log.error("kfcService服务异常！！！");
//            return "kfcService服务异常！！！";
//        }else{
//            // 否则执行原方法
//            return pjp.proceed();
//        }
//    }

//    /**
//     * 定义切入点
//     */
//    @Pointcut("execution(public * com.xf.controller.ThankController.getOk(..))")
//    public void pointcut() {}
//
//    @Before("pointcut()")
//    public void beforeScheduled(JoinPoint joinPoint) {
//        if (ObjectUtils.isNull(kfcService)) {
//            log.error("kfcService服务异常！！！");
//            try {
//                Object target = joinPoint.getTarget();
//                Method stopMethod = target.getClass().getMethod("stop");
//                stopMethod.invoke(target);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}