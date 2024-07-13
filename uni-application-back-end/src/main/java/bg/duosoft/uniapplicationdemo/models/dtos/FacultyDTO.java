package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class FacultyDTO extends BaseDTO<Long> implements Serializable {
    private Long id;

    private String facultyName;

    private Date establishedOn;

    private Integer totalNumberStudents;

    private String description;

    private List<TeacherDTO> teachers;

    private List<SpecialtyDTO> specialties;

    @Override
    public Long getId() {
        return id;
    }
}
