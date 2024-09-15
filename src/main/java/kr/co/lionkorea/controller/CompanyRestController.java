package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;
import kr.co.lionkorea.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyRestController {
    private final CompanyService companyService;

    @PostMapping("/save")
    public ResponseEntity<SaveCompanyResponse> saveCompany(@RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.saveCompany(request));
    }

    @PatchMapping("/save/{companyId}")
    public ResponseEntity<SaveCompanyResponse> updateCompany(@PathVariable("companyId") Long companyId, @RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.updateCompany(companyId, request));
    }

    @GetMapping()
    public ResponseEntity<PagedModel<FindCompaniesResponse>> findCompanies(@ModelAttribute FindCompaniesRequest request, Pageable pageable){
        return ResponseEntity.ok(companyService.findCompanies(request, pageable));
    }

}
