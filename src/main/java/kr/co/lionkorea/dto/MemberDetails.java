package kr.co.lionkorea.dto;

import kr.co.lionkorea.enums.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDetails {
    private String loginId;
    private String password;
    private Set<Role> roles;
    private String memberName;
}
