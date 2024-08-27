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

    private final Object member;

    public CustomUserDetails(Object member) {
        if (member == null) {
            throw new IllegalArgumentException("Member 값이 null이 될 수 없습니다.");
        }
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (member instanceof Member memberEntity) {
            authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().name()));
        } else if (member instanceof Admin admin) {
            authorities.add(new SimpleGrantedAuthority(admin.getRole().name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        if (member instanceof Member memberEntity) {
            return memberEntity.getPassword();
        } else if (member instanceof Admin admin) {
            return admin.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (member instanceof Member memberEntity) {
            return memberEntity.getEmail();
        } else if (member instanceof Admin admin) {
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
