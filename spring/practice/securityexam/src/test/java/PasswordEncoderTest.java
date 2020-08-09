import org.boostcourse.config.ApplicationConfig;
import org.boostcourse.config.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) // 스프링 컨테이너가 관리하는 빈을 테스트하려면 @RunWith과 @ContextConfiguration이 필요함.
@ContextConfiguration(classes = {ApplicationConfig.class, SecurityConfig.class})    // 설정 파일을 지정.
public class PasswordEncoderTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void passwordEncode() throws Exception{
        System.out.println(passwordEncoder.encode("1234")); // encode 메소드를 수행할 때마다 결과값이 다름. (passwordEncode로 인코딩된 문자열을 원래의 문자열로 바꿀 수 있는 방법은 존재하지 않는다. 이런 방식은 '단방향 암호화'라고 한다.)
    }

    @Test
    public void passwordTest() throws Exception{
        String encodepasswd = "$2a$10$hCdGC3xkuCOt9Dp8RDb38uRqVdp.cpEyWmmlhoHzC.mODdVltDvEm"; // encodepasswd의 값은 passwordEncoder.encode("1234")의 결과값 중 하나
        String password = "1234";
        boolean test = passwordEncoder.matches(password, encodepasswd); // 스프링 시큐리티는 암호가 맞는지 검사할 때 내부적으로 matches() 메서드를 이용해서 검증을 수행. (사용자가 입력한 문자열과, 암호화된 문자열을 비교하여 검증하기 때문에 암호는 인코딩된 형태로 저장이 되어 있어야한다.)
        System.out.println(test);
    }
}
