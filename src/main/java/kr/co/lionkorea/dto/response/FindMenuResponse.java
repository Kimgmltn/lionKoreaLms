package kr.co.lionkorea.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindMenuResponse {
    private Long menuId;
    private String menuName;
    private String menuLink;
    private String menuIkon;
    private Long parentMenuId;
    private List<FindMenuResponse> childMenu;

    public FindMenuResponse(Long menuId, String menuName, String menuLink, String menuIkon, Long parentMenuId) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuLink = menuLink;
        this.menuIkon = menuIkon;
        this.parentMenuId = parentMenuId;
    }
}
