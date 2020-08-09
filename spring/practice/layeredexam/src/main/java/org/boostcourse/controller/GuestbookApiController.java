package org.boostcourse.controller;

import org.boostcourse.dto.Guestbook;
import org.boostcourse.service.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(path = "/guestbooks")
public class GuestbookApiController {
    @Autowired
    GuestbookService guestbookService; //HTTP 요청 헤더를 파싱하는 중 오류 발생

    @GetMapping // "/guestbooks"로 요청이 들어왔고, contentType이 "application/json"이면서 method가 GET방식일 때 list 메서드가 실행된다.
                // "application/json" 요청이기 때문에 DispatcherServlet은 MessageConvertor를 내부적으로 사용해서 해당 Map 객체를 json으로 변환해서 전송함.
    public Map<String, Object> list(@RequestParam (name  = "start", required = false, defaultValue = "0") int start){
        List<Guestbook> list = guestbookService.getGuestbooks(start);

        int count = guestbookService.getCount();
        int pageCount = count / GuestbookService.LIMIT;

        if(count % GuestbookService.LIMIT > 0)
            pageCount++;

        List<Integer> pageStartList = new ArrayList<>();

        for(int i = 0; i < pageCount; i++){
            pageStartList.add(i * GuestbookService.LIMIT);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", count);
        map.put("pageStartLit", pageStartList);

        return map;
    }

    @PostMapping    // Guestbook은 json으로 변환되어 전송됨.
    public Guestbook write(@RequestBody Guestbook guestbook, HttpServletRequest request){
        String clientIp = request.getRemoteAddr();
        //id가 입력된 guestbook이 반환된다.

        Guestbook resultGuestbook = guestbookService.addGuestbook(guestbook, clientIp);
        System.out.println(resultGuestbook);
        return resultGuestbook;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable(name = "id") Long id, HttpServletRequest request){
        String clientIp = request.getRemoteAddr();

        int deleteCount = guestbookService.deleteGuestbook(id, clientIp);
        return Collections.singletonMap("success", deleteCount > 0 ? "true" : "false");
    }
}
