package subscribers.clearbunyang.global.security.details;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailsService
        implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AdminRepository adminRepository;
    private final MemberRepository memberRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(
            String username) throws UsernameNotFoundException {
        log.info("사용자 정보 조회: username={}", username);
        return loadUserByEmail(username);
    }

    public org.springframework.security.core.userdetails.UserDetails loadUserByEmail(String email)
            throws UsernameNotFoundException {
        log.info("사용자 정보 조회: email={}", email);

        Optional<Member> user = memberRepository.findByEmail(email);
        if (user.isPresent()) {
            return new UserDetails(user.get());
        }

        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return new UserDetails(admin.get());
        }

        throw new UsernameNotFoundException("email로 유저를 찾을 수 없습니다.: " + email);
    }
}
