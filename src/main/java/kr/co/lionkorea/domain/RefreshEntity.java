package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String refresh;
    private String expiration;

    public RefreshEntity(String username, String refresh) {
        this.username = username;
        this.refresh = refresh;
        final long EXPIRE_MS = 24 * 60 * 60 * 1000L;
        this.expiration = new Date(System.currentTimeMillis() + EXPIRE_MS).toString();
    }
}
