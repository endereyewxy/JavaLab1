package org.cyyself.URLTokenDemo.services;

import io.jsonwebtoken.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

public class LoginFromToken {
    public static final long TTL_ACCESS = 600000L; // ten minutes
    public static final String KEY_ACCESS = System.getProperty("auth.key.access");
    public static boolean doLoginFromCookie(HttpServletRequest req, HttpSession session) {
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
    public static boolean doLoginFromURLParam(HttpServletRequest req, HttpSession session) {
        String TOKEN = req.getParameter("token");
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
    public static boolean doLogin(HttpServletRequest req,HttpSession session) {
        return doLoginFromCookie(req,session) || doLoginFromURLParam(req,session);
    }
}
