package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/exam1")
public class ExceptionTestController{
    @GetMapping("exception")
    public String exceptionTest() throws Exception{
        int num = 3/0;
        return "";
    }
}
