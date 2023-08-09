package com.goit.servletm8;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;

import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Optional;
import java.util.Set;


@WebFilter(value="/time")
public class TimezoneValidateFilter extends HttpFilter {

    private static final String TIMEZONE_PARAMETER = "timezone";
    private static final String GMT_TZ = "GMT";
    private static final String UTC_TZ = "UTC";


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        String requestedTimeZone=Optional.ofNullable(req.getParameter(TIMEZONE_PARAMETER)).orElse(UTC_TZ);
        if (Set.of(GMT_TZ,UTC_TZ).contains(requestedTimeZone)) {
          chain.doFilter(req,res);
        } else if(!isValidTimeZone(requestedTimeZone)){
           res.setContentType("text/html; charset=utf-8");
           res.sendError(400,"From doFilter: Invalid TimeZone:"+requestedTimeZone);
        }else {
            chain.doFilter(req,res);
        }

    }

    private boolean isValidTimeZone(String timeZone){
        try {
             ZoneId.of(timeZone);
            return true;
        }catch(DateTimeException e){
            return false;
        }
    }
}
