package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "menu_id")
    private Long id;
    @Column(name = "menu_name")
    private String menuName;

    @OneToMany(mappedBy = "menu")
    @Builder.Default
    private List<RoleMenu> roleMenus = new ArrayList<>();

}
