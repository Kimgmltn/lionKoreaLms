package kr.co.lionkorea.dto.response;

import kr.co.lionkorea.domain.Member;
import kr.co.lionkorea.enums.Gender;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindMemberDetailResponse {
    private String memberName;
    private Gender gender;
    private String email;
    private String phoneNumber;
    private String memo;

    public static FindMemberDetailResponse entityToDto(Member member){
        return FindMemberDetailResponse.builder()
                .memberName(member.getMemberName())
                .gender(member.getGender())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .memo(member.getMemo())
                .build();
    }
}
