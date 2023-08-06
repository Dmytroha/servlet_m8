package com.goit.servletm8;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;

import jakarta.servlet.http.HttpFilter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


import java.util.Arrays;
import java.util.TimeZone;

@WebFilter(value="/time")
public class TimezoneValidateFilter extends HttpFilter {

    private static final String TIMEZONE_PARAMETER = "timezone";
    private static final String GMT_TZ = "GMT";
    private static final String UTC_TZ = "UTC";


    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String requestedTimeZone = req.getParameter(TIMEZONE_PARAMETER);
        if (requestedTimeZone == null || requestedTimeZone.equals(GMT_TZ) || requestedTimeZone.equals(UTC_TZ)) {
            chain.doFilter(req,res);
        } else if (!Arrays.asList(TimeZone.getAvailableIDs()).contains(requestedTimeZone)) {

            res.setStatus(400);

            res.setContentType("text/html; charset=utf-8");
            res.getWriter().write("Invalid Timezone: "+requestedTimeZone);
            res.getWriter().close();
        }else {
            chain.doFilter(req,res);
        }

    }
}
