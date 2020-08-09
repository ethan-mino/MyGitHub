package servlet.exam02;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logic")
public class Logic extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int v1 = (int)(Math.random() * 6) + 1;
        int v2 = (int)(Math.random() * 6) + 1;
        int result = v1 + v2;

        try{
            request.setAttribute("v1", v1);
            request.setAttribute("v2", v2);
            request.setAttribute("result", result);

            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/jsp/exam2/result.jsp");
            requestDispatcher.forward(request, response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
