package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="project")
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Project extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "project_id")
    private Long id;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "language")
    private String language;
    @Column(name = "process_status")
    private String processStatus;
    @Column(name = "consultation_date")
    private String consultationDate;
    @Column(name = "time_period")
    private String timePeriod;
    @Column(name = "hour")
    private Integer hour;
    @Column(name = "minute")
    private Integer minute;
    @Column(name = "consultation_notes")
    private String consultationNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_id", referencedColumnName = "member_id")
    private Member translator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "company_id")
    private Buyer buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domestic_id", referencedColumnName = "company_id")
    private DomesticCompany domesticCompany;
}
