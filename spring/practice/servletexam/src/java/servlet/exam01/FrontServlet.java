package servlet.exam01;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/front")
public class FrontServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        int diceValue = (int)(Math.random() * 6) + 1;
        request.setAttribute("dice", diceValue);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/next");   // forward는 같은 어플리케이션 내에서만 가능하다.
        requestDispatcher.forward(request, response);
    }
}
