package subscribers.clearbunyang.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;
import subscribers.clearbunyang.security.annotation.WithMockCustomMember;

public class WithMockCustomMemberSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomMember> {

    /**
     * @AuthenticationPrincipal 에서 admin 객체를 넣기 위해 SecurityContext를 생성하는 factory 클래스
     *
     * @param withMockCustomMember the {@link Annotation} to create the {@link SecurityContext}
     *     from. Cannot be null.
     * @return
     */
    @Override
    public SecurityContext createSecurityContext(WithMockCustomMember withMockCustomMember) {
        Member member =
                Member.builder()
                        .id(1L)
                        .email("member@gmail.com")
                        .password("password")
                        .role(UserRole.MEMBER)
                        .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
