package bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class AccreditationStatusDTO extends BaseDTO<String> implements Serializable {
    private String accreditationType;

    private String accreditationDescription;

    @Override
    public String getId() {
        return accreditationType;
    }
}
