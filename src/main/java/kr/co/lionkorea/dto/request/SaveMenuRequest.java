package kr.co.lionkorea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveMenuRequest {
    private String menuName;
    private String menuLink;
    private String menuIkon;
    private Integer orderSeq;
    private Long parentMenuId;
}
