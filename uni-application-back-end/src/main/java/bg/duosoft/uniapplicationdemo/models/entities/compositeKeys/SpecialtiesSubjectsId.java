package bg.duosoft.uniapplicationdemo.models.entities.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SpecialtiesSubjectsId implements Serializable {
    @Column(name = "specialty_id")
    private Long specialtyId;

    @Column(name = "subject_id")
    private Long subjectId;
}
