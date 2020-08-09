package org.boostcourse.controller;

import org.boostcourse.dto.Member;
import org.boostcourse.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/members")
public class MemberController {
    // 스프링 컨테이너가 생성자를 통해 자동으로 주입한다.
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    public MemberController(MemberService memberService, PasswordEncoder passwordEncoder){
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/loginform")
    public String loginForm(){
        return "members/loginform";
    }

    @RequestMapping("/loginerror")
    public String loginError(@RequestParam("login_error") String loginError){
        return "members/loginerror";
    }

    @GetMapping("/joinform")
    public String joinForm(){
        return "members/joinform";
    }

    // 사용자가 입력한 name, email, password가 member에 저장된다.
    @PostMapping("/join")
    public String join(@ModelAttribute Member member){
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberService.addMember(member);
        return "redirect:/members/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "members/welcome";
    }

    // 사용자가 로그인을 한 상태라면, 스프링 MVC는 컨트롤러 메소드에 회원정보를 저장하고 있는 Principal 객체를 파라미터로 받을 수 있다.
    @GetMapping("/memberinfo")
    public String memberInfo(Principal principal, ModelMap modelMap){   // 로그인한 회원 정보를 받기 위해 Principal을 파라미터로 선언.
        String loginId = principal.getName();   // 해당 코드를 통해 로그인 아이디를 구할 수 있다. 로그인 아이디를 구했다면, 해당 아이디를 이용해 데이터베이스로부터 회원 정보를 읽어들여 뷰에게 전달 할 수 있다.
        Member member = memberService.getMemberById(loginId);
        modelMap.addAttribute("member", member);

        return "members/memberinfo";
    }
}
