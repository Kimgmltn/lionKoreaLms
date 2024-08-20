package kr.co.lionkorea.config;

import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.RolesRepository;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("test")
@Slf4j
public class DataInitializationConfig {

    private final MemberService memberService;
    private final RolesRepository rolesRepository;

    @Bean
    public ApplicationRunner initializer(){
        return args -> {

            log.info("test 데이터를 삽입합니다.");

            log.info("Roles 데이터 삽입");

            rolesRepository.save(Roles.create(Role.ROLE_SUPER_ADMIN));
            rolesRepository.save(Roles.create(Role.ROLE_ADMIN));
            rolesRepository.save(Roles.create(Role.ROLE_TRANSLATOR));

            log.info("Roles 데이터 삽입 끝");

            log.info("Member 데이터 삽입");

            String loginId = "admin";
            SaveMemberResponse superAdmin = memberService.saveMember(new SaveMemberRequest("최상위 관리자", Gender.MAIL, "", "", ""));
            memberService.grantNewAccount(new GrantNewAccountRequest(loginId, superAdmin.getMemberId(), Role.ROLE_SUPER_ADMIN));
            List<SaveMemberRequest> list = new ArrayList<>();
            List<Long> memberIds = new ArrayList<>();
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
