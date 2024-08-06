package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_menu")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleMenu {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_menu_id")
    private Long id;
}
