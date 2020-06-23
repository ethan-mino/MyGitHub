package guestbook.servlet;

import guestbook.dao.GuestbookDao;
import guestbook.dto.Guestbook;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ConnectException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/guestbooks")
public class GuestbookListServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Guestbook> list = null;
        GuestbookDao guestbookDao = null;

        // 방명록 리스트를 얻어옴
        try{
            guestbookDao = new GuestbookDao();
            list = guestbookDao.getGuestbooks();
        }catch(Exception e){
            e.printStackTrace();
        }

        // guestbooks.jsp로 forward
        request.setAttribute("list", list);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/guestbooks.jsp");

        requestDispatcher.forward(request, response);
    }

}
