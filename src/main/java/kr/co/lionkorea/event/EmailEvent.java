package kr.co.lionkorea.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailEvent {
    private final String to;
    private final String subject;
    private final String shortUrl;
}
