package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="translator")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Translator extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "translator_id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name="gender")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name="memo")
    private String memo;
}
