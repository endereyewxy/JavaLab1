// Created by endereyewxy@gmail.com, 2020.10.17

package cn.endereye.controller;

import cn.endereye.model.User;
import cn.endereye.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/sign-in/")
public class SignIn extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            // Try to sign in using the refresh token. If failed, switch to normal sign in page instead.
            final User user = signInByRefreshToken(request);
            if (user != null)
                signInSuccess(request, response, user);
            else
                request.getRequestDispatcher("/sign-in.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            signInFailed(request, response, "数据库错误");
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
                signInSuccess(request, response, user);
            else
                signInFailed(request, response, "登录失败");
        } catch (SQLException e) {
            e.printStackTrace();
            signInFailed(request, response, "数据库错误");
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

    private void signInSuccess(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        final Cookie refreshCookie = new Cookie("refresh", AuthService.acquireRefreshToken(user));
        refreshCookie.setPath("/auth");
        final Cookie accessCookie = new Cookie("token", AuthService.acquireAccessToken(user));
        accessCookie.setPath("/");
        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);
        response.sendRedirect(request.getParameter("from"));
    }

    private void signInFailed(HttpServletRequest request, HttpServletResponse response, String error)
            throws IOException {
        request.getSession().setAttribute("errorMsg", error);
        // We must use a redirect since we need to add the `from` parameter into the URL.
        response.sendRedirect(String.format("%s?from=%s",
                request.getRequestURL(),
                URLEncoder.encode(request.getParameter("from"), "UTF-8")));
    }
}
