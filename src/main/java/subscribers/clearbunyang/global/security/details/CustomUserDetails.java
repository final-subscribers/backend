package subscribers.clearbunyang.global.security.details;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;

@Getter
@ToString
public class CustomUserDetails implements UserDetails {

    private final Object user;

    public CustomUserDetails(Object user) {
        if (user == null) {
            throw new IllegalArgumentException("User 값이 null이 될 수 없습니다.");
        }
        this.user = user;
    }

    public Long getUserId() {
        if (user instanceof Member member) {
            return member.getId();
        } else if (user instanceof Admin admin) {
            return admin.getId();
        }
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user instanceof Member member) {
            authorities.add(new SimpleGrantedAuthority(member.getRole().name()));
        } else if (user instanceof Admin admin) {
            authorities.add(new SimpleGrantedAuthority(admin.getRole().name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        if (user instanceof Member member) {
            return member.getPassword();
        } else if (user instanceof Admin admin) {
            return admin.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (user instanceof Member member) {
            return member.getEmail();
        } else if (user instanceof Admin admin) {
            return admin.getEmail();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
