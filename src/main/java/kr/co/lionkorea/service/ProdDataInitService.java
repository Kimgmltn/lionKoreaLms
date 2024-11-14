package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.dto.response.SaveMenuResponse;
import kr.co.lionkorea.dto.response.SaveRoleResponse;
import kr.co.lionkorea.enums.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProdDataInitService {

    private final MenuService menuService;
    private final RolesService rolesService;
    private final MemberService memberService;

    @Transactional
    public void dataInit(){
        if (memberService.isExistsLoginId("superAdmin")) {
            log.info("초기 설정 되어있음");
        } else{
            log.info("Roles 데이터 삽입");
            SaveRoleResponse superAdmin = rolesService.saveRole(new SaveRoleRequest("super_admin"));
            SaveRoleResponse admin = rolesService.saveRole(new SaveRoleRequest("admin"));
            SaveRoleResponse translator = rolesService.saveRole(new SaveRoleRequest("translator"));
            log.info("Roles 데이터 삽입 끝");

            log.info("Menu 데이터 삽입");
            SaveMenuResponse projectTranslatorMenu = menuService.saveMenu(new SaveMenuRequest("프로젝트", "/project/translator", "fa-diagram-project", null));
            SaveMenuResponse projectAdminMenu = menuService.saveMenu(new SaveMenuRequest("프로젝트", "/project/admin", "fa-file-shield", null));
            SaveMenuResponse memberMenu = menuService.saveMenu(new SaveMenuRequest("회원 관리", "/members", "fa-users", null));
            SaveMenuResponse companyMenu = menuService.saveMenu(new SaveMenuRequest("회사 관리", "/company/domestic", "fa-building", null));
            SaveMenuResponse buyer = menuService.saveMenu(new SaveMenuRequest("바이어 관리", "/company/buyer", "fa-building-user", null));
            log.info("Menu 데이터 삽입 끝");

            log.info("Role-Menu 데이터 삽입");
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(superAdmin.getRoleId(), List.of(projectAdminMenu.getMenuId(), memberMenu.getMenuId(), companyMenu.getMenuId(), buyer.getMenuId())));
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(admin.getRoleId(), List.of(projectAdminMenu.getMenuId(), memberMenu.getMenuId(), companyMenu.getMenuId(), buyer.getMenuId())));
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(translator.getRoleId(), List.of(projectTranslatorMenu.getMenuId())));
            log.info("Role-Menu 데이터 삽입 끝");

            log.info("Member 데이터 삽입");
            String loginId = "superAdmin";
            SaveMemberResponse superAdminMember = memberService.saveMember(new SaveMemberRequest("최상위 관리자", Gender.MALE, "", "", ""));
            memberService.grantNewAccount(superAdminMember.getMemberId(), new GrantNewAccountRequest(loginId, "super_admin"));
            log.info("Member 데이터 삽입 끝");
        }

    }
}
