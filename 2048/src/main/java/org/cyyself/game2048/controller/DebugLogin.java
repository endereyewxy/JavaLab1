package org.cyyself.game2048.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/DebugLogin")
public class DebugLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        PrintWriter out = resp.getWriter();
        String username = req.getParameter("user_id");
        resp.setHeader("system_env",System.getenv().toString());
        if (username == null || username.equals("")) {
            out.print("{\"status\":1,\"msg\":\"ERROR\"}");
        }
        else {
            session.setAttribute("user_id",username);
            out.print("{\"status\":0,\"msg\":\"OK\"}");
        }
        out.close();
    }
}
