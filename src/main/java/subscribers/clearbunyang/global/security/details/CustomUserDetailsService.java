package subscribers.clearbunyang.global.security.details;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.auth.entity.Admin;
import subscribers.clearbunyang.domain.auth.entity.Member;
import subscribers.clearbunyang.domain.auth.repository.AdminRepository;
import subscribers.clearbunyang.domain.auth.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 정보 조회: username={}", username);
        return loadUserByEmail(username);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        log.info("사용자 정보 조회: email={}", email);

        Optional<Member> user = memberRepository.findByEmail(email);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        }

        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return new CustomUserDetails(admin.get());
        }

        throw new UsernameNotFoundException("email로 유저를 찾을 수 없습니다.: " + email);
    }
}
