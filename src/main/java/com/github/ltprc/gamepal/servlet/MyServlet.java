package com.github.ltprc.gamepal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "myServlet",urlPatterns = "/srv")
public class MyServlet extends HttpServlet {

    private static final long serialVersionUID = -7739737557552701154L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //此处自行添加doGet逻辑
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //此处自行添加doPost逻辑
        super.doPost(req, resp);
    }
}
