package org.boostcourse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlusController {
    @GetMapping(path="/plusform")
    public String plusform(){   //  view name을 넘길 때, ModelAndView 객체를 리턴할 수 있고, 간단하게 String 타입으로 넘겨줄 수도 있다.
        return "plusForm";
    }

    @PostMapping(path = "/plus")
    public String plusResult(@RequestParam(name = "value1", required = true) int value1, @RequestParam(name = "value2", required = true) int value2, ModelMap modelMap){    // 파라미터에 HttpServletRequest를 선언하여 사용할 수 있지만, Web에 종속적이 되므로, ModelMap을 사용.
        int result = value1 + value2;
        modelMap.addAttribute("value1", value1);
        modelMap.addAttribute("value2", value2);
        modelMap.addAttribute("result", result);    // request scope에 매핑

        return "plusResult";    // viewName을 전달할 때, ModelAndView 객체를 리턴할수도 있고, String 타입으로 간단하게 Viewname만 넘겨줄 수 있음.
    }

}
