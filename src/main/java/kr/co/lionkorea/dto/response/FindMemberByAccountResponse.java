package kr.co.lionkorea.dto.response;

import kr.co.lionkorea.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindMemberByAccountResponse {
    private Long accountId;
    private Role role;
    private Boolean useYn;
    private Boolean passwordChangeYn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime joinDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime expireDate;
}
