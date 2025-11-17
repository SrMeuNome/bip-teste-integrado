package com.example.backend.infrastructure.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class SpaRedirectFilter implements Filter {

    private final String pathPrefix;

    public SpaRedirectFilter(@Value("${api.path}") String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) servletRequest;
            String path = req.getRequestURI();

            if (path.startsWith(pathPrefix) ||
                path.matches(".*\\.[a-zA-Z0-9]+$") ||
                path.startsWith("/actuator") ||
                path.startsWith("/error") ||
                path.startsWith("/swagger-ui")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            servletRequest.getRequestDispatcher("/index.html").forward(servletRequest, servletResponse);
        }
}