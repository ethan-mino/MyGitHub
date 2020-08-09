package org.boostcourse.controller;

import org.boostcourse.argumentresolver.HeaderInfo;
import org.boostcourse.config.ApplicationConfig;
import org.boostcourse.dao.GuestbookDao;
import org.boostcourse.dto.Guestbook;
import org.boostcourse.service.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {
    //@GetMapping(path = "/guestbook")
    //public String guestbook(){
    //    return "redirect:/guest/list";
    //}
    @Autowired
    GuestbookService guestbookService;

    @GetMapping(path = "/list")
/*    public String guestList(@RequestParam(name = "start", required = true, defaultValue = "0")int start,
                            ModelMap modelMap, HttpServletRequest request,
                            HttpServletResponse response){  // start의 값이 없다면 default로 "0"을 사용*/

    public String guestList(@RequestParam(name = "start", required = true, defaultValue = "0")int start,
                            ModelMap modelMap, @CookieValue(value = "count", defaultValue = "0", required = true) String visitCount,
                            HttpServletResponse response, HeaderInfo headerInfo){  // start의 값이 없다면 default로 "0"을 사용

        System.out.println("------------------------------------------------");
        System.out.println(headerInfo.get("user-agent"));
        System.out.println("------------------------------------------------");

        try{
            int i = Integer.parseInt(visitCount);
            visitCount = Integer.toString(++i);
        }catch (Exception e){
            visitCount = "1";
        }

        Cookie cookie = new Cookie("count", visitCount);
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유지
        cookie.setPath("/");    // 경로 이하에 모두 쿠키 적용
        response.addCookie(cookie);

        modelMap.addAttribute("visitCount", visitCount);

        /*
        String value = null;
        boolean find = false;
        Cookie[] cookies = request.getCookies();

        if(cookies != null){
            for(Cookie cookie : cookies){
                if("count".equals(cookie.getName())){
                    find = true;
                    value = cookie.getValue();
                    break;
                }
            }
        }

        if(!find){
            value = "1";
        }else{
            try{
                int i = Integer.parseInt(value);
                value = Integer.toString(++i);
            }catch (Exception e){
                value = "1";
            }
        }

        Cookie cookie = new Cookie("count", value);
        cookie.setMaxAge(60 * 60 * 24 * 365); // 1년 동안 유지
        cookie.setPath("/");    // 경로 이하에 모두 쿠키 적용
        response.addCookie(cookie);*/

        // start로 시작하는 방명록 목록 구하기
        List<Guestbook> guestbookList = guestbookService.getGuestbooks(start);

        // 전체 페이지 수 구하기
        int guestbookNum = guestbookService.getCount();
        int pageCount = guestbookNum / GuestbookService.LIMIT;

        if(guestbookNum % GuestbookService.LIMIT > 0)
            pageCount++;

        // 페이지 수만큼 start의 값을 리스트로 저장.
        // 예를 들면 페이지 수가 3이면,
        // 0, 5, 10 이렇게 저장된다.
        // list?start=0, list?start=5, list?start=10으로 링크가 걸린다.
        List<Integer> pageStartList = new ArrayList<>();
        for(int i = 0; i < pageCount; i++){
            pageStartList.add(i * GuestbookService.LIMIT);
        }

        modelMap.addAttribute("pageStartList", pageStartList);
        modelMap.addAttribute("guestbookList", guestbookList);
        modelMap.addAttribute("guestbookNum", guestbookNum);
        return "guestbookList";
    }

    @PostMapping(path = "/write")
    public String guestWrite(@ModelAttribute Guestbook guestbook){
        ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        GuestbookDao guestbookDao =  ac.getBean(GuestbookDao.class);

        guestbookDao.insert(guestbook);

        return "redirect:/guestbook/list";
    }

    @GetMapping(path = "/delete")
    public String delete(@RequestParam(name = "id", required = true) Long id, HttpServletRequest request,
                         @SessionAttribute("isAdmin") String isAdmin, RedirectAttributes redirectAttributes){
        
        if(isAdmin == null || !"true".equals(isAdmin)) {  // 세션이 생성되지 않았거나 세션값이 true가 아닌 경우
            redirectAttributes.addFlashAttribute("errorMessage", "로그인을 하지 않았습니다.");
            return "redirect:/guestbook/loginform";
        }
        String clientIp = request.getRemoteAddr();
        guestbookService.deleteGuestbook(id, clientIp);
        return "redirect:/guestbook/list";
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession session){
        session.removeAttribute("isAdmin");;
        return "redirect:/guestbook/list";
    }
}
