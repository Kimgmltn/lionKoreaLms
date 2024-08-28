package kr.co.lionkorea.config;

import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.MatchRolesAndMenusRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.request.SaveMenuRequest;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.RolesRepository;
import kr.co.lionkorea.service.MemberService;
import kr.co.lionkorea.service.MenuService;
import kr.co.lionkorea.service.RolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("test")
@Slf4j
public class DataInitializationConfig {

    private final MemberService memberService;
    private final RolesRepository rolesRepository;
    private final MenuService menuService;
    private final RolesService rolesService;

    @Bean
    public ApplicationRunner initializer(){
        return args -> {

            log.info("test 데이터를 삽입합니다.");

            log.info("Roles 데이터 삽입");
            Roles superAdmin = rolesRepository.save(Roles.create(Role.ROLE_SUPER_ADMIN));
            Roles admin = rolesRepository.save(Roles.create(Role.ROLE_ADMIN));
            Roles translator = rolesRepository.save(Roles.create(Role.ROLE_TRANSLATOR));
            log.info("Roles 데이터 삽입 끝");

            log.info("Menu 데이터 삽입");
            menuService.saveMenu(new SaveMenuRequest("프로젝트", "/project", "fa-diagram-project", null));
            menuService.saveMenu(new SaveMenuRequest("회원 관리", null, "fa-users", null));
            menuService.saveMenu(new SaveMenuRequest("번역가", "/members/translator", "fa-language", 2L));
            menuService.saveMenu(new SaveMenuRequest("관리자", "/members/admin", "fa-black-tie", 2L));
            menuService.saveMenu(new SaveMenuRequest("메뉴 관리", "/menus", "fa-bars", null));
            menuService.saveMenu(new SaveMenuRequest("대시보드", "/dashboard", "fa-compass", null));

            log.info("Menu 데이터 삽입 끝");

            log.info("Role-Menu 데이터 삽입");
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(superAdmin.getId(), List.of(1L, 2L, 3L, 4L, 5L, 6L)));
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(admin.getId(), List.of(1L, 2L, 3L, 4L, 6L)));
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(translator.getId(), List.of(1L)));
            log.info("Role-Menu 데이터 삽입 끝");

            log.info("Member 데이터 삽입");
            String loginId = "admin";
            SaveMemberResponse superAdminMember = memberService.saveMember(new SaveMemberRequest("최상위 관리자", Gender.MAIL, "", "", ""));
            memberService.grantNewAccount(new GrantNewAccountRequest(loginId, superAdminMember.getMemberId(), Role.ROLE_SUPER_ADMIN));
            for (int i = 0; i < 50; i++) {
                String name = "회원" + i;
                SaveMemberResponse saveMemberResponse = memberService.saveMember(new SaveMemberRequest(name, Gender.MAIL, "", "", ""));
                if(i < 25){
                    memberService.grantNewAccount(new GrantNewAccountRequest(loginId + i, saveMemberResponse.getMemberId(), Role.ROLE_ADMIN));
                }
                else{
                    memberService.grantNewAccount(new GrantNewAccountRequest(loginId + i, saveMemberResponse.getMemberId(), Role.ROLE_TRANSLATOR));
                }
            }
            log.info("Member 데이터 삽입 끝");





        };
    }
}
