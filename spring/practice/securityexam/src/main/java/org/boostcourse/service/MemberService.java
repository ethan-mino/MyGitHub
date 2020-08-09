package org.boostcourse.service;

import org.boostcourse.dto.Member;
import org.boostcourse.service.security.UserDbService;

import java.util.Map;

// UserDbService는 스프링 시큐리티에서 필요로 하는 정보를 가지고 오는 메소드를 가지고 있다.
public interface MemberService extends UserDbService {
    public Map<String, Long> addMember(Member member);
    public Member getMemberById(String loginId);
}
