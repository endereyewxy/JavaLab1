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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.getRequestDispatcher("/sign-up.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            final User user = UserService.signUp(new User()
                    .setUsername(request.getParameter("username"))
                    .setPasswordRaw(request.getParameter("password")));
            if (user == null)
                Redirect.failed(request, response, "用户名冲突");
            else
                Redirect.success(request, response, user);
        } catch (SQLException e) {
            e.printStackTrace();
            Redirect.failed(request, response, "数据库错误 " + e.getErrorCode());
        }
    }
}
