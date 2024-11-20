package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="company")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorColumn(name = "company_type")
public abstract class Company extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private Long id;
    @Column(name="company_name")
    private String companyName;
    @Column(name="english_name")
    private String englishName;
    @Column(name="company_registration_number")
    private String companyRegistrationNumber;
    @Column(name = "road_name_address")
    private String roadNameAddress;
    @Column(name = "products")
    private String products;
    @Column(name="homepage_url")
    private String homepageUrl;
    @Column(name="manager")
    private String manager;
    @Column(name = "email")
    private String email;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="memo")
    private String memo;
    @Column(name = "company_type", insertable = false, updatable = false)
    private String companyType;

    protected Company(Long id) {
        this.id = id;
    }

//    public static Company dtoToEntity(SaveCompanyRequest request) {
//        return Company.builder()
//                .companyName(request.getCompanyName())
//                .englishName(request.getEnglishName())
//                .companyRegistrationNumber(request.getCompanyRegistrationNumber())
//                .roadNameAddress(request.getRoadNameAddress())
//                .products(request.getProducts())
//                .homepageUrl(request.getHomepageUrl())
//                .manager(request.getManager())
//                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
//                .memo(request.getMemo())
//                .build();
//    }

    public void changeInfo(SaveCompanyRequest request) {
        this.companyName = getCompanyName().equals(request.getCompanyName()) ? getCompanyName() : request.getCompanyName();
        this.englishName = getEnglishName().equals(request.getEnglishName()) ? getEnglishName() : request.getEnglishName();
        this.companyRegistrationNumber = getCompanyRegistrationNumber().equals(request.getCompanyRegistrationNumber()) ? getCompanyRegistrationNumber() : request.getCompanyRegistrationNumber();
        this.roadNameAddress = getRoadNameAddress().equals(request.getRoadNameAddress()) ? getRoadNameAddress() : request.getRoadNameAddress();
        this.products = getProducts().equals(request.getProducts()) ? getProducts() : request.getProducts();
        this.homepageUrl = getHomepageUrl().equals(request.getHomepageUrl()) ? getHomepageUrl() : request.getHomepageUrl();
        this.manager = getManager().equals(request.getManager()) ? getManager() : request.getManager();
        this.email = getEmail().equals(request.getEmail()) ? getEmail() : request.getEmail();
        this.phoneNumber = getPhoneNumber().equals(request.getPhoneNumber()) ? getPhoneNumber() : request.getPhoneNumber();
        this.memo = getMemo().equals(request.getMemo()) ? getMemo() : request.getMemo();
    }
}
