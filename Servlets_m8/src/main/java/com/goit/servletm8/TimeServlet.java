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
import java.util.Objects;
import java.util.TimeZone;

// http://127.0.0.1:8080/Servlets_m8/time     open link in browser
//http://localhost:8080/time?timezone=UTC+2   with timezone parameter
@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd  HH:mm:ss";
    private static final String DATETIMEZONE_PATTERN = DATETIME_PATTERN+" z";
    public static final String TIMEZONE_PARAMETER = "timezone";

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    //private LocalDateTime servletStartedDateTime;
    private ZoneId serveletZoneId;
    private String servletStartedAt;
    private String servletTimeZone;
    private ZonedDateTime zonedDateTime; // Data Time in time zone
    @Override
    public void init(){
       //servletStartedDateTime = LocalDateTime.now();
        serveletZoneId = ZoneId.systemDefault() ;
        zonedDateTime= LocalDateTime.now().atZone(ZoneId.systemDefault());
        servletTimeZone = zonedDateTime.getZone().getId();
        // prepare datetime strings
        servletStartedAt = getDateTimeInTimeZoneString(serveletZoneId.getId());
        // ZoneId zoneId = ZoneId.of("UTC+4"); // for example



    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestedTimeZone = req.getParameter(TIMEZONE_PARAMETER);
        if (Objects.isNull(requestedTimeZone)){requestedTimeZone="UTC";}

        String formattedCurrentDateTime  = getDateTimeInTimeZoneString(ZoneId.systemDefault().getId());

        String formattedZonedDataTime = getDateTimeInTimeZoneString(requestedTimeZone);

        PrintWriter out = resp.getWriter();

        resp.setContentType("text/html; charset=utf-8");
        out.println("<html>");
        out.println("<head><title>Current Time</title></head>");
        out.println("<body style=\"background-color: #f0f0f0; color: #333;\">");
        out.println("<h1 style=\"color: #007bff;\">DateTime Servlet started at time: "+servletStartedAt+" ("+servletTimeZone+") </h1>");
        out.println("<h1 style=\"color: #007bff;\">Servlet Current time:</h1>");
        out.println("<p style=\"color: #28a745;\">" + formattedCurrentDateTime +" ("+servletTimeZone+ ") </p>");

        out.println("<h1 style=\"color: #007bff;\">Current time in Time Zone: </h1>");
        out.println("<p style=\"color: #28a745;\">" + formattedZonedDataTime + "( "+requestedTimeZone+ ")</p>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    private String getDateTimeInTimeZoneString(String stringZoneID){

        TimeZone.getTimeZone(stringZoneID);
        try {

            zonedDateTime = LocalDateTime.now(ZoneId.of(stringZoneID))
                    .atZone(ZoneId.of(stringZoneID));
        }catch(DateTimeException e){
            return "Invalid time zone";
        }
        // return formatted as string
        return zonedDateTime.format(DateTimeFormatter.ofPattern(DATETIMEZONE_PATTERN));
    }
}