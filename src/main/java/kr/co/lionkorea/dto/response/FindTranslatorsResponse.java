package kr.co.lionkorea.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindTranslatorsResponse {
    private Long accountId;
    private String memberName;
}
