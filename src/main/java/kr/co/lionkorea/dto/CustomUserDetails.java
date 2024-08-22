package kr.co.lionkorea.dto;

import kr.co.lionkorea.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final MemberDetails memberDetails;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        for (Role role : memberDetails.getRoles()) {
            collection.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.name();
                }
            });
        }
        return collection;
    }

    @Override
    public String getPassword() {
        return memberDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return memberDetails.getMemberName();
    }

    public Set<Role> getRoles(){
        return memberDetails.getRoles();
    }

    public Long getMemberId(){
        return memberDetails.getMemberId();
    }

}
