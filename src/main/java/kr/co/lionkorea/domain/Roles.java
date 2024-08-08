package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.enums.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roles {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name")
    @Enumerated(value = EnumType.STRING)
    private Role roleName;

    @OneToMany(mappedBy = "roles")
    private List<AccountRole> accountRoles = new ArrayList<>();
    @OneToMany(mappedBy = "roles")
    private List<RoleMenu> roleMenus = new ArrayList<>();
}
