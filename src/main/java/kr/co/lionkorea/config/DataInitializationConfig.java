package kr.co.lionkorea.config;

import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("test")
public class DataInitializationConfig {

    private final MemberService memberService;

    @Bean
    public ApplicationRunner initializer(){
        return args -> {
            memberService.saveMember(new SaveMemberRequest("최상위 관리자", Gender.MAIL, "", "", ""));
            List<SaveMemberRequest> list = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                String name = "회원" + i;
                list.add(new SaveMemberRequest(name, Gender.MAIL, "", "", ""));
            }
            memberService.saveMemberAll(list);
        };
    }
}
