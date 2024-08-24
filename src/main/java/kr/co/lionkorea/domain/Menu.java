package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.dto.request.SaveMenuRequest;
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
    @Column(name = "menu_link")
    private String menuLink;
    @Column(name = "menu_ikon")
    private String menuIkon;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RoleMenu> roleMenus = new ArrayList<>();

    public Menu(Long menuId) {
        this.id = menuId;
    }

    public static Menu dtoToEntity(SaveMenuRequest request) {
        return Menu.builder()
                .menuName(request.getMenuName())
                .menuLink(request.getMenuLink())
                .menuIkon(request.getMenuIkon())
                .build();
    }
}
