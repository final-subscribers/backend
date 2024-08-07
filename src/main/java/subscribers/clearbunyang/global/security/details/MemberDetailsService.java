package subscribers.clearbunyang.global.security.details;


import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import subscribers.clearbunyang.domain.member.entity.Admin;
import subscribers.clearbunyang.domain.member.entity.User;
import subscribers.clearbunyang.domain.member.repository.AdminRepository;
import subscribers.clearbunyang.domain.member.repository.UserRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("사용자 정보 조회: username={}", username);
        return loadUserByEmail(username);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        log.info("사용자 정보 조회: email={}", email);

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new MemberDetails(user.get());
        }

        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return new MemberDetails(admin.get());
        }

        throw new UsernameNotFoundException("email로 유저를 찾을 수 없습니다.: " + email);
    }
}
