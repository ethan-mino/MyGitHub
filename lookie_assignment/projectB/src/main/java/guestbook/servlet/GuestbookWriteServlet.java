package guestbook.servlet;

import guestbook.dao.GuestbookDao;
import guestbook.dto.Guestbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/guestbooks/write")
public class GuestbookWriteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");  // form 태그의 한글이 깨지지 않도록 함

        String name = null;
        String content = null;

        // 입력 폼의 데이터를 얻어옴
        try{
            name = request.getParameter("name");
            content = request.getParameter("content");
        }catch(Exception e){
            e.printStackTrace();
        }

        // dto 객체 생성
        Guestbook guestbook = new Guestbook(name, content);

        // dao 객체 생성
        GuestbookDao guestbookDao = new GuestbookDao();

        // 데이터베이스에 데이터를 추가
        guestbookDao.addGuestbook(guestbook);

        // guestbooks로 redirect
        response.sendRedirect("/guestbooks");
    }
}
