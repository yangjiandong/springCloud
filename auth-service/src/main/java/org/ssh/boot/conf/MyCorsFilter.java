package org.ssh.boot.conf;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.ssh.boot.security.jwt.TokenAuthenticationService;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*"); // request.getHeader("Origin")
        // response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        // response.setHeader("Access-Control-Max-Age", "3600");
        String[] headers = {"X-Requested-With", "MUserAgent", "mtoken", "uid", "Content-Type",
            TokenAuthenticationService.HEADER_STRING};
        response.setHeader("Access-Control-Allow-Headers",
            String.join(",",
                headers));//);// Content-Type, Accept, X-Requested-With, remember-me, MUserAgent, mtoken, uid

        //log.info("MyCorsFilter get request headers: {}", request.getHeaderNames().toString());
        // for (Enumeration one = request.getHeaderNames(); one.hasMoreElements(); ) {
        //     String headerName = (String) one.nextElement();
        //     System.out.println("MyCorsFilter get request headers Name = " + headerName);
        // }

        // log.info("MyCorsFilter get url: {}, {}", request.getRequestURL().toString(),
        //    request.getMethod());
        if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(req, res);
        }
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        return map;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    // void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    //     HttpServletResponse response = (HttpServletResponse) res
    //     response.setHeader("Access-Control-Allow-Origin", "*")
    //     response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE")
    //     response.setHeader("Access-Control-Allow-Headers", "x-requested-with")
    //     response.setHeader("Access-Control-Max-Age", "3600")
    //     if (request.getMethod()!='OPTIONS') {
    //         chain.doFilter(req, res)
    //     } else {
    //     }
    // }
    //
    // void init(FilterConfig filterConfig) {}
    //
    // void destroy() {}
}
