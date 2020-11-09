package org.cyyself.game2048.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import org.cyyself.game2048.services.LoginFromToken;
import cn.endereye.util.MD5;
import io.jsonwebtoken.*;

@WebServlet(urlPatterns = "/Login")
public class Login extends HttpServlet {
    public static final long TTL_ACCESS = 600000L; // ten minutes
    public static final String KEY_ACCESS =
            Objects.requireNonNull(MD5.md5("KEY_ACCESS" + System.getenv().toString()));
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (LoginFromToken.doLogin(req,req.getSession())) {
            resp.sendRedirect("/2048/game");
        }
        else {
            resp.sendRedirect("/auth/sign-in/?from=/2048/Login");
        }
    }
}
