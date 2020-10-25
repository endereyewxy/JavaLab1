package cn.endereye.controller;

import cn.endereye.model.User;
import cn.endereye.service.UserService;
import cn.endereye.util.Redirect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/sign-up/")
public class SignUp extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            User user = UserService.signUp(new User()
                    .setUsername(req.getParameter("username"))
                    .setPasswordRaw(req.getParameter("password")));
            Redirect.success(req, resp, user);
        } catch (SQLException e) {
            e.printStackTrace();
            Redirect.failed(req, resp, "数据库错误");
        }
    }
}
