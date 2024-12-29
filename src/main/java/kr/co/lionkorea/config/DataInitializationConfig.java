package kr.co.lionkorea.config;

import kr.co.lionkorea.domain.FileStorage;
import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.FileStorageRepository;
import kr.co.lionkorea.repository.RolesRepository;
import kr.co.lionkorea.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CompanyService companyService;
    private final ProjectService projectService;
    private final FileStorageRepository fileStorageRepository;

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
            menuService.saveMenu(new SaveMenuRequest("프로젝트", "/projects/admin", "fa-file-shield", 1, null));
            menuService.saveMenu(new SaveMenuRequest("프로젝트", "/projects/translator", "fa-diagram-project", 2, null));
            menuService.saveMenu(new SaveMenuRequest("회원 관리", "/members", "fa-users", 3, null));
//            menuService.saveMenu(new SaveMenuRequest("번역가", "/members/translator", "fa-language", 2L));
//            menuService.saveMenu(new SaveMenuRequest("관리자", "/members/admin", "fa-hammer", 2L));
//            menuService.saveMenu(new SaveMenuRequest("메뉴 관리", "/menus", "fa-bars", null));
            menuService.saveMenu(new SaveMenuRequest("회사 관리", "/company/domestic", "fa-building", 4, null));
            menuService.saveMenu(new SaveMenuRequest("바이어 관리", "/company/buyer", "fa-building-user", 5, null));
//            menuService.saveMenu(new SaveMenuRequest("대시보드", "/dashboard", "fa-compass", null));

            log.info("Menu 데이터 삽입 끝");

            log.info("Role-Menu 데이터 삽입");
//            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(superAdmin.getId(), List.of(1L, 2L, 3L, 4L, 5L, 6L)));
//            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(admin.getId(), List.of(1L, 2L, 3L, 4L, 6L)));
//            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(translator.getId(), List.of(1L)));

            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(superAdmin.getId(), List.of(1L, 3L, 4L,5L)));
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(admin.getId(), List.of(1L, 3L, 4L,5L)));
            rolesService.matchRolesAndMenus(new MatchRolesAndMenusRequest(translator.getId(), List.of(2L)));
            log.info("Role-Menu 데이터 삽입 끝");

            log.info("Member 데이터 삽입");
            String loginId = "admin";
            SaveMemberResponse superAdminMember = memberService.saveMember(new SaveMemberRequest("최상위 관리자", Gender.MALE, "", "", ""));
            memberService.grantNewAccount(superAdminMember.getMemberId(), new GrantNewAccountRequest("superAdmin", "super_admin"));
            for (int i = 0; i < 50; i++) {
                String name = "회원" + i;
                String email = "email" + i + "@email.com";
                SaveMemberResponse saveMemberResponse = memberService.saveMember(new SaveMemberRequest(name, Gender.MALE, email, "", ""));
                if(i < 25){
                    memberService.grantNewAccount(saveMemberResponse.getMemberId(), new GrantNewAccountRequest(loginId + i, "admin"));
                }
                else{
                    memberService.grantNewAccount(saveMemberResponse.getMemberId(), new GrantNewAccountRequest(loginId + i, "translator"));
                }
            }
            log.info("Member 데이터 삽입 끝");

            log.info("Company 데이터 삽입");
            for (int i = 0; i < 30; i++) {
                String companyName = "회사" + i;
                String managerName = "매니저" + i;
                companyService.saveDomesticCompany(new SaveCompanyRequest(companyName, "", "", "", "", "", managerName, "", "", ""));
            }

            for (int i = 30; i < 60; i++) {
                String companyName = "바이어" + i;
                String managerName = "매니저" + i;
                companyService.saveBuyer(new SaveCompanyRequest(companyName, "", "", "", "", "", managerName, "", "", ""));
            }
            log.info("Company 데이터 삽입 끝");

            log.info("Project 데이터 삽입");
            String localDate = LocalDate.now().toString();
            for (int i = 0; i< 24; i++) {
                String projectName = "프로젝트" + i;
                long translatorId = 26L + i;
                long buyerId =31L + i;
                long domesticCompanyId =1L + i;
                if(i < 12){
                    if( i < 4){
                        translatorId = 51L;
                    }
                    int h = 1 + i;
                    String hour = String.format("%02d", h);
                    projectService.saveProject(new SaveProjectRequest(buyerId, domesticCompanyId, projectName, translatorId, "영어", localDate, hour, "00", "AM", null));
                }else{
                    int h = 1 + (i - 12);
                    String hour = String.format("%02d", h);
                    projectService.saveProject(new SaveProjectRequest(buyerId, domesticCompanyId, projectName, translatorId, "영어", localDate, hour, "00", "PM", null));
                }
            }
            log.info("Project 데이터 삽입 끝");

            log.info("file storage 데이터 삽입");
            fileStorageRepository.save(FileStorage.creatFileStorage("translationcompany-excel-form", "members", ".xlsx"));
            fileStorageRepository.save(FileStorage.creatFileStorage("translationcompany-excel-form", "domesticCompany", ".xlsx"));
            log.info("file storage 데이터 삽입 끝");
        };
    }
}
