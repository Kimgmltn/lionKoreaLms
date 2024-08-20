package kr.co.lionkorea.dto.request;

import kr.co.lionkorea.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrantNewAccountRequest {
    private String loginId;
    private String password;
    private Role role;
    private Long memberId;

    public GrantNewAccountRequest(String loginId, Long memberId) {
        this.loginId = loginId;
        this.memberId = memberId;
    }

    public GrantNewAccountRequest(String loginId, Long memberId, Role role) {
        this.loginId = loginId;
        this.memberId = memberId;
        this.role = role;
    }
}
