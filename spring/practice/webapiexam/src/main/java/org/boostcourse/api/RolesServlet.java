package org.boostcourse.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.boostcourse.dao.RoleDao;
import org.boostcourse.dto.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/roles")
public class RolesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        RoleDao roleDao = new RoleDao();

        List<Role> roleList = roleDao.getRoles();

        ObjectMapper objectMapper = new ObjectMapper(); // json 문자열로 바꾸거나 json 문자열을 객체로 바꾸는 역할을 수행하는 객체.
        String json = objectMapper.writeValueAsString(roleList);    // list를 json 문자열로 변환

        PrintWriter out = response.getWriter();
        out.println(json);
        out.close();
    }
}
