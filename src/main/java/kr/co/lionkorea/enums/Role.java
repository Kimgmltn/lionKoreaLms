package kr.co.lionkorea.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_SUPER_ADMIN("최상위 관리자", "super_admin"),
    ROLE_ADMIN("관리자", "admin"),
    ROLE_TRANSLATOR("번역가", "translator");

    private final String koreanName;
    private final String englishName;
}
