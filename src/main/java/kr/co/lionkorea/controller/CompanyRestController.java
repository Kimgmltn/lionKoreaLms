package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.dto.response.FindCompanyDetailResponse;
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

    @PostMapping("/domestic/save")
    public ResponseEntity<SaveCompanyResponse> saveDomesticCompany(@RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.saveDomesticCompany(request));
    }

    @PostMapping("/buyer/save")
    public ResponseEntity<SaveCompanyResponse> saveBuyer(@RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.saveBuyer(request));
    }

    @PatchMapping("/domestic/save/{companyId}")
    public ResponseEntity<SaveCompanyResponse> updateDomesticCompany(@PathVariable("companyId") Long companyId, @RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.updateDomesticCompany(companyId, request));
    }

    @PatchMapping("/buyer/save/{companyId}")
    public ResponseEntity<SaveCompanyResponse> updateBuyer(@PathVariable("companyId") Long companyId, @RequestBody SaveCompanyRequest request){
        return ResponseEntity.ok(companyService.updateBuyer(companyId, request));
    }

    @GetMapping("/domestic")
    public ResponseEntity<PagedModel<FindCompaniesResponse>> findDomesticCompanies(@ModelAttribute FindCompaniesRequest request, Pageable pageable, @RequestParam("companyName") String companyName){
        return ResponseEntity.ok(companyService.findDomesticCompanies(request, pageable, companyName));
    }

    @GetMapping("/buyer")
    public ResponseEntity<PagedModel<FindCompaniesResponse>> findBuyers(@ModelAttribute FindCompaniesRequest request, Pageable pageable, @RequestParam("companyName") String companyName){
        return ResponseEntity.ok(companyService.findBuyers(request, pageable, companyName));
    }

    @GetMapping("/domestic/{companyId}")
    public ResponseEntity<FindCompanyDetailResponse> findDomesticCompanyById(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.findDomesticCompanyById(companyId));
    }

    @GetMapping("/buyer/{companyId}")
    public ResponseEntity<FindCompanyDetailResponse> findBuyerById(@PathVariable Long companyId){
        return ResponseEntity.ok(companyService.findBuyerById(companyId));
    }

}
