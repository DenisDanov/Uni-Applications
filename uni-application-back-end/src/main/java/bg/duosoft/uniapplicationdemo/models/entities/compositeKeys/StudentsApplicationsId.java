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
public class StudentsApplicationsId implements Serializable {

    @Column(name = "username")
    private String username;

    @Column(name = "specialty_id")
    private Long specialtyId;

    @Column(name = "faculty_id")
    private Long facultyId;
}
