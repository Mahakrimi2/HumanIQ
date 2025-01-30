package pfe.HumanIQ.HumanIQ.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pfe.HumanIQ.HumanIQ.models.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    String userName;
    String password;
    List<GrantedAuthority> authorities;
    public UserInfoDetails(User user){
        userName= user.getUserName();
        password= user.getPassword();
        authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
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
