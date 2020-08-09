package org.boostcourse.service;

import org.boostcourse.dao.MemberDao;
import org.boostcourse.dao.MemberRoleDao;
import org.boostcourse.dto.Member;
import org.boostcourse.dto.MemberRole;
import org.boostcourse.service.security.UserEntity;
import org.boostcourse.service.security.UserRoleEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


// 로그인한 사용자 id를 파라미터로 받아들여서 UserEntity와 List<UserRoleEntity>를 리턴하는 메소드를 가지고 있다.
// 로그인 정보가 저장된 데이터베이스의 구조는 프로젝트마다 다르기 때문에 스프링 시큐리티에서 알 수 없다.
// 로그인 정보가 어디에 저장되어 있든 해당 인터페이스를 구현하는 쪽에 맡긴다.
// MemberServiceImpl클래스는 MemberService 인터페이스를 구현한다. MemberService는 회원과 관련된 기능을 가지게 된다.
// MemberService는 UserDbService를 상속받았으며, UserDbService는 스프링 시큐리티에서 필요로 하는 정보를 가지고 오는 메소드를 가지고 있다.
// MemberService 인터페이스를 구현한다는 것은 UserDbService 역시 구현해야 한다는 것을 의미한다.
// 데이터베이스에서 읽어들이는 코드와 스프링 시큐리티에서 사용되는 코드를 분리
@Service
public class MemberServiceImpl implements MemberService{
    // 생성자에 의 해 주입되는 객체이고, 해당 객체를 초기화할 이후에 없기 때문에 final로 선언하였다.
    // final로 선언하고 초기화를 안한 필드는 생성자에서 초기화를 해준다.
    private final MemberDao memberDao;
    private final MemberRoleDao memberRoleDao;

    // @Service가 붙은 객체는 스프링이 자동으로 Bean으로 생성하는데
    // 기본 생성자가 없고 아래와 같이 인자를 받는 생성자만 있을 경우, 자동으로 관련되 타입이 Bean으로 있다면 주입해서 사용하게 된다.
    public MemberServiceImpl(MemberDao memberDao, MemberRoleDao memberRoleDao){
        this.memberDao = memberDao;
        this.memberRoleDao = memberRoleDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(String loginId){
        Member member = memberDao.getMemberByEmail(loginId);
        return member;
    }

    @Override
    @Transactional(readOnly = false)
    public Map<String, Long> addMember(Member member){
        // member row 추가.
        member.setCreateDate(new Date());
        member.setModifyDate(new Date());
        Long memberId = memberDao.addMember(member);
        member.setId(memberId);

        // memberRole row 추가.
        MemberRole memberRole = new MemberRole(member.getId(), MemberRole.USER);
        Long memberRoleId = memberRoleDao.addMemberRole(memberRole);

        Map<String, Long> idMap = new HashMap<>();
        idMap.put("memberId", memberId);
        idMap.put("memberRoleId", memberRoleId);

        return idMap;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getUser(String loginUserId) {
        Member member = memberDao.getMemberByEmail(loginUserId);
        return new UserEntity(member.getEmail(), member.getPassword());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserRoleEntity> getUserRoles(String loginUserId) {
        List<MemberRole> memberRoles = memberRoleDao.getRolesByEmail(loginUserId);
        List<UserRoleEntity> list = new ArrayList<>();

        for(MemberRole memberRole : memberRoles){
            list.add(new UserRoleEntity(loginUserId, memberRole.getRoleName()));
        }

        return list;
    }
}
