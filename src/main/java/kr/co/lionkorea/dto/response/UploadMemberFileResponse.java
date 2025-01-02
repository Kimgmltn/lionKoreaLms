package kr.co.lionkorea.dto.response;

import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadMemberFileResponse {
    List<SaveMemberRequest> memberRequests;
    List<GrantNewAccountRequest> accountRequests;
}
