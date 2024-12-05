package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "short_url_account_map")
@Getter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShortUrlAccountMap {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "map_id")
    private Long id;
    @Column(name = "short_url", unique = true, nullable = false)
    private String shortUrl;
    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    public static ShortUrlAccountMap createEntity(String shortUrl, Long accountId){
        return ShortUrlAccountMap.builder()
                .shortUrl(shortUrl)
                .accountId(accountId)
                .build();
    }
}
