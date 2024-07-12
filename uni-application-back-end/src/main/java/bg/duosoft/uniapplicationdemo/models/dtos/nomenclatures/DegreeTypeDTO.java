package bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class DegreeTypeDTO extends BaseDTO<String> implements Serializable {
    private String degreeType;

    private String degreeDescription;

    @Override
    public String getId() {
        return degreeType;
    }
}
