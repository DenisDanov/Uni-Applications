package bg.duosoft.uniapplicationdemo.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "specialties", schema = "uni_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SpecialtyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "specialty_name", nullable = false, unique = true)
    private String specialtyName;

    @Column(name = "total_credits_required")
    private Integer totalCreditsRequired;

    @Column(name = "employment_rate")
    private Double employmentRate;

    @ManyToOne
    @JoinColumn(name = "faculty_id", referencedColumnName = "id", nullable = false)
    private FacultyEntity faculty;

    @ManyToOne
    @JoinColumn(name = "specialty_requirement_id", referencedColumnName = "id", nullable = false)
    private SpecialtyRequirementEntity specialtyRequirement;

    @ManyToOne
    @JoinColumn(name = "specialty_program_id", referencedColumnName = "id", nullable = false)
    private SpecialtyProgramEntity specialtyProgram;

    @ManyToMany
    @JoinTable(schema = "uni_applications", name = "specialties_subjects", joinColumns = @JoinColumn(name = "specialty_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"))
    private List<SubjectEntity> subjects;

}
