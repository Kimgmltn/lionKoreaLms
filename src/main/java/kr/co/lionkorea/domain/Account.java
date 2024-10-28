package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveAccountDetailRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="account_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId;
    @Column(name = "password")
    private String password;
    @Column(name = "join_date")
    private LocalDateTime joinDate;
    @Column(name="expire_date")
    private LocalDateTime expireDate;
    @Column(name = "random_password_change_yn")
    private Boolean randomPasswordChangeYn;

    @Column(name = "use_yn")
    private Boolean useYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL)
    @Builder.Default
    private List<AccountRole> accountRoles = new ArrayList<>();

    public static Account dtoToEntity(GrantNewAccountRequest request, Member member, Roles roles) {
        LocalDateTime now = LocalDateTime.now();
        Account account = Account.builder()
                .loginId(request.getLoginId())
                .password(request.getPassword())
                .joinDate(now)
                .expireDate(now.plusYears(30L))
                .randomPasswordChangeYn(false)
                .useYn(true)
                .member(member)
                .build();
        account.addRole(roles);
        return account;
    }

    public void addRole(Roles roles){
        this.accountRoles.add(new AccountRole(this, roles));
    }

    public void removeRole(Roles roles) {
        getAccountRoles().removeIf(accountRole -> accountRole.getRoles().equals(roles));
    }

    public void changeUseYn(SaveAccountDetailRequest request) {
        this.useYn = request.isUseYn();
    }
}
