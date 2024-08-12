package subscribers.clearbunyang.global.security.details;


import java.util.Collection;
import java.util.HashSet;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;

@Getter
@ToString
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private final Object member;

    public UserDetails(Object member) {
        if (member == null) {
            throw new IllegalArgumentException("Member 값이 null이 될 수 없습니다.");
        }
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        if (member instanceof Member) {
            Member member = (Member) this.member;
            authorities.add(new SimpleGrantedAuthority(member.getUserRole().name()));
        } else if (member instanceof Admin) {
            Admin admin = (Admin) member;
            authorities.add(new SimpleGrantedAuthority(admin.getUserRole().name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        if (member instanceof Member) {
            return ((Member) member).getPassword();
        } else if (member instanceof Admin) {
            return ((Admin) member).getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if (member instanceof Member) {
            return ((Member) member).getEmail();
        } else if (member instanceof Admin) {
            return ((Admin) member).getEmail();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return org.springframework.security.core.userdetails.UserDetails.super
                .isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return org.springframework.security.core.userdetails.UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return org.springframework.security.core.userdetails.UserDetails.super
                .isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return org.springframework.security.core.userdetails.UserDetails.super.isEnabled();
    }
}
