// Created by endereyewxy@gmail.com, 2020.10.17

package cn.endereye.controller;

import cn.endereye.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            final String jwtToken = cn.endereye.service.Login.verifyPassword(new User(
                    request.getParameter("username"),
                    request.getParameter("password")
            ));
            if (jwtToken == null) {
                declareError(request, response, "登录失败");
            } else {
                final Cookie cookie = new Cookie("jwt", jwtToken);
                cookie.setPath("/");
                response.addCookie(cookie);
                response.sendRedirect(request.getParameter("from"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            declareError(request, response, "数据库错误");
        }
    }

    @SuppressWarnings("deprecation")
    private void declareError(HttpServletRequest request, HttpServletResponse response, String error)
            throws IOException {
        response.sendRedirect(String.format("/?from=%s&error=%s",
                URLEncoder.encode(request.getParameter("from")),
                URLEncoder.encode((error))));
    }
}
