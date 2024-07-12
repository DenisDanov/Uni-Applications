package bg.duosoft.uniapplicationdemo.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class DeclinedStudentApplicationDTO implements Serializable {
    private String username;
    private Long facultyId;
    private Long specialtyId;
    private LocalDate deleteDate;
}
