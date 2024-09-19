package subscribers.clearbunyang.security;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.entity.enums.UserRole;
import subscribers.clearbunyang.global.security.details.CustomUserDetails;
import subscribers.clearbunyang.security.annotation.WithMockCustomAdmin;

public class WithMockCustomAdminSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomAdmin> {

    /**
     * @AuthenticationPrincipal 에서 admin 객체를 넣기 위해 SecurityContext를 생성하는 factory 클래스
     *
     * @param withMockCustomUser the {@link Annotation} to create the {@link SecurityContext} from.
     *     Cannot be null.
     * @return
     */
    @Override
    public SecurityContext createSecurityContext(WithMockCustomAdmin withMockCustomUser) {
        Admin admin =
                Admin.builder()
                        .id(1L)
                        .email("admin@gmail.com")
                        .password("password")
                        .role(UserRole.ADMIN)
                        .build();
        CustomUserDetails customUserDetails = new CustomUserDetails(admin);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
