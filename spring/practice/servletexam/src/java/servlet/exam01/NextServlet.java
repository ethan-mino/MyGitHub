package servlet.exam01;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/next")
public class NextServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        int diceValue = (int)request.getAttribute("dice");

        out.println("<html>");
        out.println("<body>");
        out.println("diceValue : " + diceValue + "<br>");

        for(int i = 0; i < diceValue; i++)
            out.println("Hello World </br>");

        out.println("</body>");

        out.println("</html>");
    }
}
