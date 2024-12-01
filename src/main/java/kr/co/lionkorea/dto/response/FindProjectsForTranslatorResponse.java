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
public class FindProjectsForTranslatorResponse {
    private Long projectId;
    private String buyerName;
    private String domesticCompanyName;
    private String language;
    private String projectName;
    private ProcessStatus processStatus;
    private String translatorName;
    private String timePeriod;
    private String hour;
    private String minute;
    private String rejectReason;
}
