package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
public class SpecialtyRequirementDTO extends BaseDTO<Long> implements Serializable {
    private Long id;

    private Double minGrade;

    private String requirementDetails;

    private Double languageProficiencyTestMinResult;

    private Double standardizedTestMinResult;

    private Boolean letterOfRecommendationRequired;

    private Boolean personalStatementRequired;

    @Override
    public Long getId() {
        return id;
    }
}
