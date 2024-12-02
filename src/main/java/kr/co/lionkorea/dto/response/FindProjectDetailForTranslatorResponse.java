package kr.co.lionkorea.dto.response;

import kr.co.lionkorea.enums.ProcessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindProjectDetailForTranslatorResponse {
    private Long projectId;
    private String buyerName;
    private String domesticCompanyName;
    private String projectName;
    private String translatorName;
    private String language;
    private ProcessStatus processStatus;
    private String consultationDate;
    private String timePeriod;
    private String hour;
    private String minute;
    private String consultationNotes;
    private String rejectReason;
}
