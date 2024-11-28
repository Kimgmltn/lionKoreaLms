package kr.co.lionkorea.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindProjectDetailForAdminResponse {
    private Long projectId;
    private String buyerName;
    private String domesticCompanyName;
    private String projectName;
    private String translatorName;
    private String language;
    private String processStatus;
    private String consultationDate;
    private String timePeriod;
    private String hour;
    private String minute;
    private String consultationNotes;
}
