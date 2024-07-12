package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.ApplicationStatusDTO;
import bg.duosoft.uniapplicationdemo.models.entities.compositeKeys.StudentsApplicationsId;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class StudentApplicationDTO extends BaseDTO<StudentsApplicationsId> implements Serializable {
    private String username;

    private Long specialtyId;

    private Long facultyId;

    private Date applicationSentDate;

    private ApplicationStatusDTO applicationStatus;

    private String applicationDescription;

    private Double avgGrade;

    private Double languageProficiencyTestResult;

    private Double standardizedTestResult;

    private String letterOfRecommendation;

    private String personalStatement;

    @Override
    public StudentsApplicationsId getId() {
        return new StudentsApplicationsId(username,specialtyId,facultyId);
    }
}
