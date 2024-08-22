package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.enums.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roles {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Long id;
    @Column(name = "role_name")
    @Enumerated(value = EnumType.STRING)
    private Role roleName;

    @OneToMany(mappedBy = "roles", orphanRemoval = true)
    @Builder.Default
    private List<AccountRole> accountRoles = new ArrayList<>();
    @OneToMany(mappedBy = "roles", cascade = CascadeType.MERGE, orphanRemoval = true)
    @Builder.Default
    private List<RoleMenu> roleMenus = new ArrayList<>();

    public static Roles create(Role roleName) {
        return Roles.builder()
                .roleName(roleName)
                .build();
    }

    public void addMenu(Menu menu) {
        RoleMenu roleMenu = new RoleMenu(this, menu);
        this.roleMenus.add(roleMenu);
    }
    public void addMenus(List<Menu> menus) {
        for (Menu menu : menus) {
            addMenu(menu);
        }
    }
}
