package bg.duosoft.uniapplicationdemo.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "faculties", schema = "uni_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FacultyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculty_name", nullable = false)
    private String facultyName;

    @Column(name = "established_on", nullable = false)
    private Date establishedOn;

    @Column(name = "total_number_students")
    private Integer totalNumberStudents;

    private String description;

    @OneToMany(mappedBy = "faculty")
    private List<SpecialtyEntity> specialties;

    @ManyToMany
    @JoinTable(schema = "uni_applications", name = "faculties_teachers",
            joinColumns = @JoinColumn(name = "faculty_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"))
    private List<TeacherEntity> teachers;

}
