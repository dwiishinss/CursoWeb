package com.lemonsoftware.lemonmoney.lemonmoney_api.api.config;

import java.io.IOException;
import java.util.Map;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if("/auth/refresh".equalsIgnoreCase(req.getRequestURI())
        && req.getCookies() != null){
            for(Cookie cookie : req.getCookies()){
                if(cookie.getName().equals("refreshToken")){
                    String refreshToken = cookie.getValue();
                    req = new MyServletRequestWrapper(req, refreshToken);
                }
            }
        }

        chain.doFilter(req, response);
    }
    

    static class MyServletRequestWrapper extends HttpServletRequestWrapper{

        private String refreshToken;

        public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
            super(request);
            this.refreshToken = refreshToken;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
            map.put("refreshToken", new String[] {refreshToken});
            map.setLocked(true);

            return map;
        }
        
        @Override
        public String getParameter(String name) {
            if ("refreshToken".equals(name)) {
                return refreshToken;
            }
            return super.getParameter(name);
        }

        @Override
        public String[] getParameterValues(String name) {
            if ("refreshToken".equals(name)) {
                return new String[] { refreshToken };
            }
            return super.getParameterValues(name);
        }
        
    }
}
