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

@WebServlet("/roles/*")
public class RoleByIdServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        RoleDao roleDao = new RoleDao();
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        String idStr = pathParts[1];
        int id = Integer.parseInt(idStr);

        Role role = roleDao.getRollById(id);
        
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(role);

        PrintWriter out = response.getWriter();
        out.println(json);
        out.close();
    }
}
