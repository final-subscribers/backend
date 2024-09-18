package subscribers.clearbunyang.testfixtures;


import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;

public class MemberRegisterFixture {
    public static Member createDefault() {
        return Member.builder()
                .name("Default Member")
                .email("default@member.com")
                .password("password123")
                .phoneNumber("01012345678")
                .role(UserRole.MEMBER)
                .build();
    }
}
