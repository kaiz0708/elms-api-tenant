package com.elms.api.cfg.component;


import com.elms.api.exception.UnauthorizationException;
import com.elms.api.service.LoggingService;
import com.elms.api.service.impl.UserServiceImpl;
import com.elms.api.cfg.tenants.TenantDBContext;
import com.elms.api.jwt.QRJwt;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    LoggingService loggingService;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws IOException {
        if (DispatcherType.REQUEST.name().equals(request.getDispatcherType().name())
                && request.getMethod().equals(HttpMethod.GET.name())) {
            loggingService.logRequest(request, null);
        }

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        log.error("Starting call url: [" + getUrl(request) + "]");

        QRJwt jwt = userService.getAddInfoFromToken();
        log.error("Token: {}", jwt);
        String tenantName = request.getHeader("X-tenant");
        if(jwt!=null && jwt.getDeviceId()!=null && jwt.getTenantId()!=null){
            TenantDBContext.setCurrentTenant(jwt.getTenantId().split("&")[0]);
            return true;
        }else if(tenantName != null){
            if(jwt != null){
                List<String> tenantContextList = Arrays.asList(jwt.getTenantId().split(":"));
                for (String tenantContext : tenantContextList){
                    if(tenantContext.split("&")[0].equals(tenantName)){
                        TenantDBContext.setCurrentTenant(tenantName);
                        return true;
                    }
                }
            }else{
                TenantDBContext.setCurrentTenant(tenantName);
                return true;
            }
        }
        // loi tenant
        throw new UnauthorizationException("Invalid tenant: "+ TenantDBContext.getCurrentTenant());
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        log.error("Complete [" + getUrl(request) + "] executeTime : " + executeTime + "ms");

        if (ex != null) {
            log.error("afterCompletion>> " + ex.getMessage());

        }
    }

    /**
     * get full url request
     *
     * @param req
     * @return
     */
    private static String getUrl(HttpServletRequest req) {
        String reqUrl = req.getRequestURL().toString();
        String queryString = req.getQueryString();   // d=789
        if (!StringUtils.isEmpty(queryString)) {
            reqUrl += "?" + queryString;
        }
        return reqUrl;
    }
}
