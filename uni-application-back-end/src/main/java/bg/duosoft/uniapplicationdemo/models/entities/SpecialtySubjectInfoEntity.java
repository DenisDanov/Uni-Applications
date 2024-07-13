package bg.duosoft.uniapplicationdemo.models.entities;

import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.SpecialtiesSubjectsId;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "specialties_subjects_info", schema = "uni_applications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SpecialtySubjectInfoEntity implements Serializable {

    @EmbeddedId
    private SpecialtiesSubjectsId id;

    @Column(name = "duration_hours", nullable = false)
    private Double durationHours;
}
