package org.cyyself.game2048.services;

import cn.endereye.util.MD5;
import io.jsonwebtoken.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

public class LoginFromToken {
    public static final long TTL_ACCESS = 600000L; // ten minutes
    public static final String KEY_ACCESS =
            Objects.requireNonNull(MD5.md5("KEY_ACCESS" + System.getenv().toString()));
    public static boolean doLogin(HttpServletRequest req, HttpSession session) {
        Cookie[] cookies = req.getCookies();
        String TOKEN = null;
        if (cookies != null) for (Cookie cookie : cookies) if (cookie.getName().equals("token")) TOKEN = cookie.getValue();
        if (TOKEN != null && !TOKEN.equals("")) {
            Claims claims;
            try {
                claims = Jwts.parser().setSigningKey(KEY_ACCESS.getBytes()).parseClaimsJws(TOKEN).getBody();
                if (claims.getIssuedAt().getTime() - new Date().getTime() < TTL_ACCESS && claims.get("username") != null) {
                    session.setAttribute("user_id",(String)claims.get("username"));
                    return true;
                }
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
                return false;
            }
        }
        return false;
    }
}
