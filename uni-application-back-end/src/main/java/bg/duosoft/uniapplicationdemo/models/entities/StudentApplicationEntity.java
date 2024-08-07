package bg.duosoft.uniapplicationdemo.models.entities;

import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.ApplicationStatusEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "students_applications", schema = "uni_applications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StudentApplicationEntity implements Serializable {

    @EmbeddedId
    private StudentsApplicationsId id;

    @Column(insertable = false, updatable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SpecialtyEntity specialty;

    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FacultyEntity faculty;

    @Column(name = "application_sent_date", nullable = false)
    private Date applicationSentDate;

    @ManyToOne
    @JoinColumn(name = "application_status", referencedColumnName = "application_status")
    private ApplicationStatusEntity applicationStatus;

    @Column(name = "application_description", nullable = false)
    private String applicationDescription;

    @Column(name = "avg_grade", nullable = false)
    private Double avgGrade;

    @Column(name = "language_proficiency_test_result")
    private Double languageProficiencyTestResult;

    @Column(name = "standardized_test_result", nullable = false)
    private Double standardizedTestResult;

    @Column(name = "letter_of_recommendation")
    private byte[] letterOfRecommendation;

    @Column(name = "personal_statement")
    private String personalStatement;
}
