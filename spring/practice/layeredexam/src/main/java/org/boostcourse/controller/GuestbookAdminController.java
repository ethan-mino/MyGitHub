package org.boostcourse.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/guestbook")
public class GuestbookAdminController {
    @GetMapping(path="/loginform")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping(path = "/login")
    public String login(@RequestParam(name = "password", required = true) String password, HttpSession session, RedirectAttributes redirectAttributes){

        if("1234".equals(password)){
            session.setAttribute("isAdmin", "true");
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "암호가 틀렸습니다."); // redirectAttributes는 DispathcerServlet이 관리하는 FlashMap에 값을 저장 (리다이렉트할 때 딱 한번만 값을 유지할 목적으로 사용)
            return "redirect:/guestbook/loginform";
        }
        return "redirect:/guestbook/list";
    }
}
