package bg.duosoft.uniapplicationdemo.models.entities;

import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.DeclinedStudentApplicationId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "declined_student_applications", schema = "uni_applications")
@EqualsAndHashCode
@NoArgsConstructor
public class DeclinedStudentApplicationEntity implements Serializable {

    @EmbeddedId
    private DeclinedStudentApplicationId id;

    @Column(name = "delete_date", nullable = false)
    private LocalDate deleteDate;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "username", referencedColumnName = "username"),
            @JoinColumn(name = "specialty_id", referencedColumnName = "specialty_id"),
            @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id")
    })
    private StudentApplicationEntity studentApplication;

}
