package com.goit.servletm8;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

// http://127.0.0.1:8080/Servlets_m8/time     open link in browser
//http://localhost:8080/time?timezone=UTC+2   with timezone parameter
// http://127.0.0.1:8080/Servlets_m8/time?timezone=America/New_York
@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd  HH:mm:ss";
    private static final String DATETIME_ZONE_PATTERN = DATETIME_PATTERN+" z";
    public static final String TIMEZONE_PARAMETER = "timezone";

    private String servletTimeZone;
    private ZonedDateTime zonedDateTime; // Data Time in time zone
    @Override
    public void init(){
        zonedDateTime= LocalDateTime.now().atZone(ZoneId.systemDefault());
        servletTimeZone = zonedDateTime.getZone().getId();

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestedTimeZone=Optional.ofNullable(req.getParameter(TIMEZONE_PARAMETER)).orElse("UTC");
        String formattedCurrentDateTime  = getDateTimeInTimeZoneString(ZoneId.systemDefault().getId());
        String formattedZonedDataTime = getDateTimeInTimeZoneString(requestedTimeZone);

        PrintWriter out = resp.getWriter();

        resp.setContentType("text/html; charset=utf-8");
        out.println("<html>");
        out.println("<head><title>Current Time</title></head>");
        out.println("<body style=\"background-color: #f0f0f0; color: #333;\">");
        out.println("<h1 style=\"color: #007bff;\">Servlet Current time:</h1>");
        out.println("<p style=\"color: #28a745;\">" + formattedCurrentDateTime +" ("+servletTimeZone+ ") </p>");

        out.println("<h1 style=\"color: #007bff;\">Current time in Time Zone:" + " ("+requestedTimeZone + ") </h1>");
        out.println("<p style=\"color: #28a745;\">" + formattedZonedDataTime + "</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    private String getDateTimeInTimeZoneString(String stringZoneID){


        try{
            ZoneId zoneId = ZoneId.of(stringZoneID);
            zonedDateTime = LocalDateTime.now(zoneId)
                    .atZone(zoneId);
        }catch(DateTimeException e){
            return "From doGet: Invalid time zone:"+stringZoneID;
        }
        // return formatted as string
        return zonedDateTime.format(DateTimeFormatter.ofPattern(DATETIME_ZONE_PATTERN));
    }
}