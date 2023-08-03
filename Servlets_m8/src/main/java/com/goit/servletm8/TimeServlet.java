package com.goit.servletm8;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.*;


@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    private  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss z");
    private LocalDateTime currentDateTime;

    @Override
    public void init(){

        currentDateTime = LocalDateTime.now();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String formattedCurrentDateTime = currentDateTime.format(dtf);
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("<head><title>Current Time</title></head>");
        out.println("<body>");
        out.println("<h1> Поточний час (UTC):</h1>");
        out.println("<p>" + formattedCurrentDateTime + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
}
