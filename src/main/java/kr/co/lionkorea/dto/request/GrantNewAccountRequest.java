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
    private String role;
    private String expireDate;
    private boolean useYn;
    private String to;

    public GrantNewAccountRequest(String loginId) {
        this.loginId = loginId;
    }

    public GrantNewAccountRequest(String loginId, String role) {
        this.loginId = loginId;
        this.role = role;
        this.useYn = true;
    }

    public GrantNewAccountRequest(String loginId, String role, boolean useYn) {
        this.loginId = loginId;
        this.role = role;
        this.useYn = useYn;
    }
}
