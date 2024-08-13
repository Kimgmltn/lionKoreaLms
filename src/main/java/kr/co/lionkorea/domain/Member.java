package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;
    @Column(name="member_name")
    private String memberName;
    @Enumerated(value = EnumType.STRING)
    @Column(name="gender")
    private Gender gender;
    @Column(name="email")
    private String email;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name = "memo")
    private String memo;

    @OneToMany(mappedBy = "member")
    private List<Account> account = new ArrayList<>();

    public static Member dtoToEntity(SaveMemberRequest request) {
        return Member.builder()
                .memberName(request.getMemberName())
                .gender(request.getGender())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .memo(request.getMemo())
                .build();
    }

    public void changeInfo(SaveMemberRequest request) {
        this.memberName = getMemberName().equals(request.getMemberName()) ? getMemberName() : request.getMemberName();
        this.gender = getGender().equals(request.getGender()) ? getGender() : request.getGender();
        this.email = getEmail().equals(request.getEmail()) ? getEmail() : request.getEmail();
        this.phoneNumber = getPhoneNumber().equals(request.getPhoneNumber()) ? getPhoneNumber() : request.getPhoneNumber();
        this.memo = getMemo().equals(request.getMemo()) ? getMemo() : request.getMemo();
    }
}
