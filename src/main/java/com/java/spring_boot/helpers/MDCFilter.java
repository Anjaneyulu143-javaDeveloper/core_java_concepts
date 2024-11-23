package com.java.spring_boot.helpers;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

// second case .
@Component
public class MDCFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try{
            String name = request.getHeader("userId");
            String username = UUID.randomUUID().toString();
            MDC.put("userId",username);
            MDC.put("correlationId",UUID.randomUUID().toString());
            filterChain.doFilter(request,response);
        } finally {
            // MDC.remove("userId");
            MDC.clear();
        }
    }
}
