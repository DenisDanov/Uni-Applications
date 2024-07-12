package bg.duosoft.uniapplicationdemo.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "specialties_requirements", schema = "uni_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SpecialtyRequirementEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_grade", nullable = false)
    private Double minGrade;

    @Column(name = "requirement_details", nullable = false)
    private String requirementDetails;

    @Column(name = "language_proficiency_test_min_result")
    private Double languageProficiencyTestMinResult;

    @Column(name = "standardized_test_min_result", nullable = false)
    private Double standardizedTestMinResult;

    @Column(name = "letter_of_recommendation_required", nullable = false)
    private Boolean letterOfRecommendationRequired;

    @Column(name = "personal_statement_required", nullable = false)
    private Boolean personalStatementRequired;

}
