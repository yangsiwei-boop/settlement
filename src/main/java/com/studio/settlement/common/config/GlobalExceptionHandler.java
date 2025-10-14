package com.studio.settlement.common.config;

import cn.dev33.satoken.util.SaResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        e.printStackTrace();
        if (StringUtils.contains(e.getMessage(), "Token无效")
                || StringUtils.contains(e.getMessage(), "未能读取到有效Token")
                || StringUtils.contains(e.getMessage(), "Token已被顶下线")){
            return SaResult.error("请重新登录").setCode(401);
        }
        return SaResult.error(e.getMessage());
    }

}
