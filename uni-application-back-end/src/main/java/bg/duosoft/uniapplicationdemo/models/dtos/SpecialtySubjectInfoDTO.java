package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.SpecialtiesSubjectsId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class SpecialtySubjectInfoDTO extends BaseDTO<SpecialtiesSubjectsId> implements Serializable {
    private Long specialtyId;

    private Long subjectId;

    private Double durationHours;

    @Override
    public SpecialtiesSubjectsId getId() {
        return new SpecialtiesSubjectsId(this.specialtyId, this.subjectId);
    }
}
