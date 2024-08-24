package kr.co.lionkorea.dto.response;

import kr.co.lionkorea.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindMemberResponse {
    private Long memberId;
    private String memberName;
    private Gender gender;
    private String email;
}
