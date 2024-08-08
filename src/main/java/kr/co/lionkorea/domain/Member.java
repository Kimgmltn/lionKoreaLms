package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
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
}
