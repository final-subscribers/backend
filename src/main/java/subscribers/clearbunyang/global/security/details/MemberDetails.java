package subscribers.clearbunyang.global.security.details;


import java.util.Collection;
import java.util.HashSet;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import subscribers.clearbunyang.domain.member.entity.Admin;
import subscribers.clearbunyang.domain.member.entity.Company;
import subscribers.clearbunyang.domain.member.entity.User;

@Getter
@ToString
public class MemberDetails implements UserDetails {

    private final Object member;

    public MemberDetails(Object member) {
        if (member == null) {
            throw new IllegalArgumentException("Member 값이 null이 될 수 없습니다.");
        }
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        if (member instanceof User) {
            User user = (User) member;
            authorities.add(new SimpleGrantedAuthority(user.getMemberRole().name()));
        } else if (member instanceof Admin) {
            Admin admin = (Admin) member;
            authorities.add(new SimpleGrantedAuthority(admin.getMemberRole().name()));
        } else {
            Company company = (Company) member;
            authorities.add(new SimpleGrantedAuthority(company.getMemberRole().name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        if (member instanceof User) {
            return ((User) member).getPassword();
        } else if (member instanceof Admin) {
            return ((Admin) member).getPassword();
        } else return ((Company) member).getPassword();
    }

    @Override
    public String getUsername() {
        if (member instanceof User) {
            return ((User) member).getEmail();
        } else if (member instanceof Admin) {
            return ((Admin) member).getEmail();
        } else return ((Company) member).getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
