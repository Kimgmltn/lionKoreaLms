package kr.co.lionkorea.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaveProjectRequest {
    private Long buyerId;
    private Long domesticCompanyId;
    private String projectName;
    private Long translatorId;
    private String language;
    private String consultationDate;
    private String hour;
    private String minute;
    private String timePeriod;
    private String consultationNotes;
}
