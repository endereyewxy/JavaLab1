package org.cyyself.game2048.controller;

import org.cyyself.game2048.services.LoginFromToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/index.jsp")
public class IndexPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        if (session.getAttribute("user_id") == null) LoginFromToken.doLogin(req,session);
        if (session.getAttribute("user_id") != null) {
            Boolean jump_sync = (Boolean) session.getAttribute("jump_sync");
            if (jump_sync == null) jump_sync = true;
            session.setAttribute("jump_sync",!jump_sync);
            if (jump_sync) {
                req.getRequestDispatcher("/sync.jsp").forward(req,resp);
                return;
            }
        }
        req.getRequestDispatcher("/game.jsp").forward(req,resp);
    }
}
