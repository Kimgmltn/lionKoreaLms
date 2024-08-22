package kr.co.lionkorea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchRolesAndMenusRequest {
    private Long roleId;
    private List<Long> menuIds;
}
