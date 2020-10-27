// Created by endereyewxy@gmail.com, 2020.10.21

package cn.endereye.controller;

import cn.endereye.model.User;
import cn.endereye.service.AuthService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/sign-out/")
public class SignOut extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final User user = SignIn.signInByRefreshToken(request);
            if (user != null)
                AuthService.signOut(user);
            response.sendRedirect("../sign-in/"); // TODO Maybe redirect to another location?
        } catch (SQLException e) {
            e.printStackTrace(); // TODO Now what?
        }
    }
}
