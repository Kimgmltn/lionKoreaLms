package kr.co.lionkorea.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("B")
@Getter
@SuperBuilder
//@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Buyer extends Company{

    public Buyer(Long companyId) {
        super(companyId);
    }

    public static Buyer dtoToEntity(SaveCompanyRequest request) {
        return Buyer.builder()
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
