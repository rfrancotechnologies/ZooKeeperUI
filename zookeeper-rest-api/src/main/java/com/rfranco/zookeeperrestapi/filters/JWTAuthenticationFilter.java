package com.rfranco.zookeeperrestapi.filters;

import com.rfranco.zookeeperrestapi.services.CookieAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {
    private CookieAuthenticationService cookieAuthenticationService;

    public JWTAuthenticationFilter(CookieAuthenticationService cookieAuthenticationService) {
        this.cookieAuthenticationService = cookieAuthenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = cookieAuthenticationService.getAuthentication((HttpServletRequest)request);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        chain.doFilter(request,response);
    }
}