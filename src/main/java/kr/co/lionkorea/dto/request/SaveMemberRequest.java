package kr.co.lionkorea.dto.request;

import kr.co.lionkorea.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveMemberRequest {
    private String memberName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String memo;
}
