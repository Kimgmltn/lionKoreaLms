package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "file_storage")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FileStorage extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "file_id")
    private Long id;
    @Column(name = "bucket_name", nullable = false)
    private String bucketName;
    @Column(name = "object_name", unique = true)
    private String objectName;
    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    public static FileStorage creatFileStorage(String bucketName, String objectName, String fileExtension){
        return FileStorage.builder()
                .bucketName(bucketName)
                .objectName(objectName)
                .fileExtension(fileExtension)
                .build();

    }
}
