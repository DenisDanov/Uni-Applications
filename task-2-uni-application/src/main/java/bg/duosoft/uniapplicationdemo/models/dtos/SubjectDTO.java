package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class SubjectDTO extends BaseDTO<Long> implements Serializable {
    private Long id;

    private String subjectName;

    private String subjectDescription;

    @Override
    public Long getId() {
        return id;
    }
}
