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
public class FindProjectsByCompanyIdResponse {
    private Long projectId;
    private Long buyerId;
    private Long domesticCompanyId;
    private String buyerName;
    private String domesticCompanyName;
    private String projectName;
    private ProcessStatus processStatus;
    private String consultationDate;
}
