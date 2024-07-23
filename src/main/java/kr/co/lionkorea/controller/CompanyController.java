package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @PostMapping("/save")
    public ResponseEntity<?> saveCompany(@RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.saveCompany(request));
    }

}
