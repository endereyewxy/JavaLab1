package cn.endereye.util;

import cn.endereye.model.User;
import cn.endereye.service.AuthService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public abstract class Redirect {
    public static void success(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        final Cookie refreshCookie = new Cookie("refresh", AuthService.acquireRefreshToken(user));
        refreshCookie.setPath("/auth");
        response.addCookie(refreshCookie);
        if ("cookie".equals(request.getParameter("method"))) {
            final Cookie accessCookie = new Cookie("token", AuthService.acquireAccessToken(user));
            accessCookie.setPath("/");
            response.addCookie(accessCookie);
            response.sendRedirect(request.getParameter("from"));
        } else {
            response.sendRedirect(request.getParameter("from") + "?token=" + AuthService.acquireAccessToken(user));
        }
    }

    public static void failed(HttpServletRequest request, HttpServletResponse response, String error)
            throws IOException {
        request.getSession().setAttribute("errorMsg", error);
        // We must use a redirect since we need to add the `from` parameter into the URL.
        String method = request.getParameter("method");
        if (method == null)
            method = "cookie";
        response.sendRedirect(String.format("%s?from=%s&method=%s",
                request.getRequestURL(),
                URLEncoder.encode(request.getParameter("from"), "UTF-8"),
                URLEncoder.encode(method, "UTF-8")));
    }
}
