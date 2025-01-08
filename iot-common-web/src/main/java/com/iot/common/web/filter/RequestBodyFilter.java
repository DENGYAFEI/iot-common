package com.iot.common.web.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter( urlPatterns = "/**", filterName = "RequestBodyFilter" )
@Order( 1 )
public class RequestBodyFilter implements Filter {
    @Override
    public void init( FilterConfig filterConfig ) throws ServletException {
        Filter.super.init( filterConfig );
    }

    @Override
    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {
        if ( servletRequest instanceof HttpServletRequest ) {
            servletRequest = new RequestWrapper( ( HttpServletRequest ) servletRequest );
        }
        // 获取请求中的流如何，将取出来的字符串，再次转换成流，然后把它放入到新request对象中
        // 在chain.doFiler方法中传递新的request对象
        filterChain.doFilter( servletRequest, servletResponse );
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
