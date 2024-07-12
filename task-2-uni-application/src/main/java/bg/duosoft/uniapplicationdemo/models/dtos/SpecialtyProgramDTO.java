package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccreditationStatusDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.DegreeTypeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class SpecialtyProgramDTO extends BaseDTO<Long> implements Serializable {
    private Long id;

    private Date startsOn;

    private Date endsOn;

    private Integer durationDays;

    private String programName;

    private DegreeTypeDTO degreeType;

    private String description;

    private AccreditationStatusDTO accreditationStatus;

    @Override
    public Long getId() {
        return id;
    }
}
