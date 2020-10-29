package org.cyyself.game2048.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.cyyself.game2048.model.DataResult;
import org.cyyself.game2048.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/GetData")
public class GetData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();
        if (session.getAttribute("user_id") == null) {
            out.print("{\"status\":1,\"msg\":\"Please Login First\"}");
        }
        else {
            User user = new User((String)session.getAttribute("user_id"));
            DataResult result = new DataResult(0,"OK",user.getScore(),user.getStatus());
            System.out.println(result.toString());
            Gson gson = new GsonBuilder().create();
            out.print(gson.toJson(result));
        }
        out.close();
    }
}
