package org.cyyself.game2048.controller;

import org.cyyself.game2048.model.UserScore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/UpdateScore")
public class UpdateScore extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String score = req.getParameter("score");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        if (session.getAttribute("user_id") == null) {
            out.print("{\"status\":1,\"msg\":\"Please Login First\"}");
        }
        else {
            UserScore us = new UserScore((String) session.getAttribute("user_id"));
            us.SetScore(Integer.parseInt(score));
            out.print("{\"status\":0,\"msg\":\"OK\"}");
        }
        out.close();
    }
}
