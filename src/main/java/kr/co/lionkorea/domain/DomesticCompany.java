package kr.co.lionkorea.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("D")
@Getter
@SuperBuilder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DomesticCompany extends Company{

    public static DomesticCompany dtoToEntity(SaveCompanyRequest request) {
        return DomesticCompany.builder()
                .companyName(request.getCompanyName())
                .englishName(request.getEnglishName())
                .companyRegistrationNumber(request.getCompanyRegistrationNumber())
                .roadNameAddress(request.getRoadNameAddress())
                .products(request.getProducts())
                .homepageUrl(request.getHomepageUrl())
                .manager(request.getManager())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .memo(request.getMemo())
                .build();
    }
}
