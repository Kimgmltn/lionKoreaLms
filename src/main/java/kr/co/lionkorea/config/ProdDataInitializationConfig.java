package kr.co.lionkorea.config;

import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.dto.response.SaveMenuResponse;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.RolesRepository;
import kr.co.lionkorea.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Profile("default")
@Configuration
@Slf4j
public class ProdDataInitializationConfig {

    private final ProdDataInitService prodDataInitService;

    @Bean
    public ApplicationRunner initializer(){
        return args -> {

            log.info("Prod 데이터를 삽입합니다.");

            prodDataInitService.dataInit();
        };
    }
}
