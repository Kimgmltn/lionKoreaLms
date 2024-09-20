package kr.co.lionkorea.dto.response;

import kr.co.lionkorea.domain.Company;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCompanyDetailResponse {
    private String companyName;
    private String englishName;
    private String companyRegistrationNumber;
    private String roadNameAddress;
    private String products;
    private String homepageUrl;
    private String manager;
    private String email;
    private String phoneNumber;
    private String memo;

    public static FindCompanyDetailResponse entityToDto(Company company) {
        return FindCompanyDetailResponse.builder()
                .companyName(company.getCompanyName())
                .englishName(company.getEnglishName())
                .companyRegistrationNumber(company.getCompanyRegistrationNumber())
                .roadNameAddress(company.getRoadNameAddress())
                .products(company.getProducts())
                .homepageUrl(company.getHomepageUrl())
                .manager(company.getManager())
                .email(company.getEmail())
                .phoneNumber(company.getPhoneNumber())
                .memo(company.getMemo())
                .build();
    }
}
