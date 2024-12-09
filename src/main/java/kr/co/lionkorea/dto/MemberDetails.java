package kr.co.lionkorea.dto;

import kr.co.lionkorea.enums.Role;
import lombok.*;

import java.util.Set;


/**
 * Filter에서 사용하는 customUserDetail에 들어가는 클래스
 */
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
    private Long memberId;
    private Long accountId;
}
