package org.boostcourse.controller;

import org.boostcourse.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistController {
    @RequestMapping(path = "/userform", method = RequestMethod.GET)
    public String userForm(){
        return "userForm";
    }

    @RequestMapping(path = "/regist", method = RequestMethod.POST)
    public String regist(@ModelAttribute User user){    // RequestParam을 사용해서 하나씩 받지 않아도 dto를 통해서 한번에 받을 수 있음
        System.out.println("사용자가 입력한 user 정보입니다. 해당 정보를 이용하는 코드가 와야합니다.");
        System.out.println(user);
        return "regist";
    }
 }

