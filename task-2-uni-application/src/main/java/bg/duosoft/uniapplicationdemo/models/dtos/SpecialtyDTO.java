package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyDTO extends BaseDTO<Long> implements Serializable {
    private Long id;

    private Long facultyID;

    private String specialtyName;

    private Integer totalCreditsRequired;

    private Double employmentRate;

    private SpecialtyRequirementDTO specialtyRequirement;

    private SpecialtyProgramDTO specialtyProgram;

    private List<SubjectDTO> subjects;

    @Override
    public Long getId() {
        return id;
    }
}
