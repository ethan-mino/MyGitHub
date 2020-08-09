package org.boostcourse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping("/main")
    public String main(){
        return "members/main"; // @ResponseBody 어노테이셔을 사용했기 때문에 문자열 그대로를 응답
    }

    @RequestMapping("/securepage")  // 403으로 응답함. (해당 경로는 인증을 거친 후에만 접근할 수 있음을 의미)
    @ResponseBody
    public String securityPage(){
        return "secure page";
    }
}
