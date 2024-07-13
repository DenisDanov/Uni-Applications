package bg.duosoft.uniapplicationdemo.models.entities;

import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.AccreditationStatusEntity;
import bg.duosoft.uniapplicationdemo.models.entities.nomenclatures.DegreeTypeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "specialties_programs", schema = "uni_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SpecialtyProgramEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "starts_on", nullable = false)
    private Date startsOn;

    @Column(name = "ends_on", nullable = false)
    private Date endsOn;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "program_name")
    private String programName;

    @ManyToOne
    @JoinColumn(name = "degree_type", referencedColumnName = "degree_type", nullable = false)
    private DegreeTypeEntity degreeType;

    private String description;

    @ManyToOne
    @JoinColumn(name = "accreditation_type", referencedColumnName = "accreditation_type")
    private AccreditationStatusEntity accreditationStatus;
}
