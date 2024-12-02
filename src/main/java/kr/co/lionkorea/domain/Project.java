package kr.co.lionkorea.domain;

import jakarta.persistence.*;
import kr.co.lionkorea.dto.request.SaveProjectRequest;
import kr.co.lionkorea.enums.ProcessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="projects")
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
    @Enumerated(value = EnumType.STRING)
    @Column(name = "process_status")
    private ProcessStatus processStatus;
    @Column(name = "consultation_date")
    private String consultationDate;
    @Column(name = "time_period")
    private String timePeriod;
    @Column(name = "consultation_hour")
    private String hour;
    @Column(name = "consultation_minute")
    private String minute;
    @Column(name = "consultation_notes")
    private String consultationNotes;
    @Column(name = "reject_reason")
    private String rejectReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_id", referencedColumnName = "account_id")
    private Account translator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "company_id")
    private Buyer buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domestic_id", referencedColumnName = "company_id")
    private DomesticCompany domesticCompany;

    public static Project dtoToEntity(SaveProjectRequest request){
        return Project.builder()
                .projectName(request.getProjectName())
                .language(request.getLanguage())
                .processStatus(ProcessStatus.WAITING)
                .consultationDate(request.getConsultationDate())
                .timePeriod(request.getTimePeriod())
                .hour(request.getHour())
                .minute(request.getMinute())
                .consultationNotes(request.getConsultationNotes())
                .translator(new Account(request.getTranslatorId()))
                .buyer(new Buyer(request.getBuyerId()))
                .domesticCompany(new DomesticCompany(request.getDomesticCompanyId()))
                .build();
    }

    public void rejectProject(String rejectReason) {
        this.rejectReason = rejectReason;
        this.processStatus = ProcessStatus.REJECT;
    }

    public void startProject(){
        this.processStatus = ProcessStatus.PROGRESS;
    }

    public void completeProject(String consultationNotes){
        this.processStatus = ProcessStatus.COMPLETED;
        this.consultationNotes = consultationNotes;
    }

}
