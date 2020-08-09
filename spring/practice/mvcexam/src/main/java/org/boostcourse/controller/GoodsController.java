package org.boostcourse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class GoodsController {
    @GetMapping("/goods/{id}")
    public String getGoodsById(@PathVariable(name="id") int id, @RequestHeader (value = "User-agent", defaultValue = "myBrowser") String userAgent,
                               HttpServletRequest request, ModelMap modelMap){  // HttpServletRequest를 사용하는 것은 권장하지 않음.
        String path = request.getServletPath();

        System.out.println("id : " + id);
        System.out.println("user_agent : " + userAgent);
        System.out.println("path : " + path);

        modelMap.addAttribute("id", id);
        modelMap.addAttribute("user_agent", userAgent);
        modelMap.addAttribute("path", path);
        return "goodsById";
    }
}
