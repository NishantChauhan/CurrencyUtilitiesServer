package com.nishant.springboot.currencyutilities.restcontrollers.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Service
public class HttpRequestInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, @Nullable HttpServletResponse response,
                             @Nullable Object handler) {
        Enumeration<String> headers = request.getHeaderNames();
        StringBuilder headersLine = new StringBuilder(1024);
        headersLine.append("\nHeaders = { ");
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            headersLine.append(header).append(" : ").append(request.getHeader(header)).append(" , ");
        }
        headersLine.append(" }");
        logger.info(headersLine.toString());
        return true;
    }
}
