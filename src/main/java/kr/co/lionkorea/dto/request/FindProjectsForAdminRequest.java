package kr.co.lionkorea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindProjectsForAdminRequest {
    private String consultationDate;
    private String buyerName;
    private String domesticCompanyName;
    private String translatorName;
}
