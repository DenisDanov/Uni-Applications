package bg.duosoft.uniapplicationdemo.models.entities.compositeKeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DeclinedStudentApplicationId implements Serializable {

    @Column(name = "username", insertable = false, updatable = false)
    private String username;

    @Column(name = "specialty_id", insertable = false, updatable = false)
    private Long specialtyId;

    @Column(name = "faculty_id", insertable = false, updatable = false)
    private Long facultyId;
}