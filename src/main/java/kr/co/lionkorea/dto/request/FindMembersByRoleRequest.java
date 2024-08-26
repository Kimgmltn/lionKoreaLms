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
public class FindMembersByRoleRequest {
    private Role role;
}
