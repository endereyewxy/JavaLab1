// Created by endereyewxy@gmail.com, 2020.10.17

package cn.endereye.controller;

import cn.endereye.model.User;
import cn.endereye.service.AuthService;
import cn.endereye.util.Redirect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/sign-in/")
public class SignIn extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            // Try to sign in using the refresh token. If failed, switch to normal sign in page instead.
            // Only try to do this when not displaying error message.
            if (request.getSession().getAttribute("errorMsg") == null) {
                final User user = signInByRefreshToken(request);
                if (user != null) {
                    Redirect.success(request, response, user);
                    return;
                }
            }
            request.getRequestDispatcher("/sign-in.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            Redirect.failed(request, response, "数据库错误 " + e.getErrorCode());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            final User user = AuthService.signInByUsernameAndPassword(new User()
                    .setUsername(request.getParameter("username"))
                    .setPasswordRaw(request.getParameter("password")));
            if (user != null)
                Redirect.success(request, response, user);
            else
                Redirect.failed(request, response, "登录失败");
        } catch (SQLException e) {
            e.printStackTrace();
            Redirect.failed(request, response, "数据库错误 " + e.getErrorCode());
        }
    }

    static User signInByRefreshToken(HttpServletRequest request) throws SQLException {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                // Find the cookie named "refresh".
                // There should be just one cookie named this, but we will take the last one anyway.
                if ("refresh".equals(cookie.getName()))
                    refreshToken = cookie.getValue();
            }
        }
        if (refreshToken != null)
            // Try to sign in using the refresh token. If failed, switch to normal sign in page instead.
            return AuthService.signInByRefreshToken(refreshToken);
        else
            return null;
    }
}
