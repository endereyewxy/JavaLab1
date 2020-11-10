package org.cyyself.URLTokenDemo.controller;

import org.cyyself.URLTokenDemo.services.LoginFromToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(urlPatterns = "/Login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (LoginFromToken.doLogin(req,req.getSession())) {
            resp.sendRedirect("/URLTokenDemo/index.jsp");
        }
        else {
            resp.sendRedirect("http://auth.javalab1.cyyself.name:8080/auth/sign-in/?from="+ req.getRequestURL().toString() + "&method=url");
        }
    }
}