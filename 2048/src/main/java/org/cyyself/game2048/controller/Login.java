package org.cyyself.game2048.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import cn.endereye.util.MD5;
import io.jsonwebtoken.*;

@WebServlet(urlPatterns = "/Login")
public class Login extends HttpServlet {
    public static final long TTL_ACCESS = 600000L; // ten minutes
    public static final String KEY_ACCESS =
            Objects.requireNonNull(MD5.md5("KEY_ACCESS" + System.getenv().toString()));

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        String TOKEN = null;
        for (Cookie cookie : cookies) if (cookie.getName().equals("token")) TOKEN = cookie.getValue();
        if (TOKEN != null) {
            Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(KEY_ACCESS.getBytes()).parseClaimsJws(TOKEN).getBody();
                if (claims.getIssuedAt().getTime() - new Date().getTime() < TTL_ACCESS && claims.get("username") != null) {
                    HttpSession session = req.getSession();
                    session.setAttribute("user_id",(String)claims.get("username"));
                    resp.sendRedirect("/2048/game");
                    return;
                }
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            }
        }
        resp.sendRedirect("/auth/sign-in/?from=/2048/Login");
    }
}
