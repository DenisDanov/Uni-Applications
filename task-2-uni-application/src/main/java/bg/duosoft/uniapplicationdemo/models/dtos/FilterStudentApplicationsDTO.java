package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FilterStudentApplicationsDTO extends StudentApplicationDTO implements Serializable {
    private String facultyName;
    private String specialtyName;
    private int maxResults;
}
