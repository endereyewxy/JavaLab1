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
        final Cookie accessCookie = new Cookie("token", AuthService.acquireAccessToken(user));
        accessCookie.setPath("/");
        response.addCookie(refreshCookie);
        response.addCookie(accessCookie);
        response.sendRedirect(request.getParameter("from"));
    }

    public static void failed(HttpServletRequest request, HttpServletResponse response, String error)
            throws IOException {
        request.getSession().setAttribute("errorMsg", error);
        // We must use a redirect since we need to add the `from` parameter into the URL.
        response.sendRedirect(String.format("%s?from=%s",
                request.getRequestURL(),
                URLEncoder.encode(request.getParameter("from"), "UTF-8")));
    }
}
