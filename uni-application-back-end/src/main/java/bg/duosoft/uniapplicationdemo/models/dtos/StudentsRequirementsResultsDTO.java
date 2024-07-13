package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentsRequirementsResultsDTO extends BaseDTO<String> implements Serializable {
    private String username;
    private Double languageProficiencyTestResult;
    private Double standardizedTestResult;
}
