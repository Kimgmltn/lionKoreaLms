package kr.co.lionkorea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveCompanyRequest {
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
}
