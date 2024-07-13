package bg.duosoft.uniapplicationdemo.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "students_requirements_results", schema = "uni_applications")
public class StudentsRequirementsResultsEntity implements Serializable {

    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "language_proficiency_test_result")
    private Double languageProficiencyTestResult;

    @Column(name = "standardized_test_result", nullable = false)
    private Double standardizedTestResult;
}