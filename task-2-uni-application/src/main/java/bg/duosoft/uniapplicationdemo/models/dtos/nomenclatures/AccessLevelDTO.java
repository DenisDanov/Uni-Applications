package bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class AccessLevelDTO extends BaseDTO<String> implements Serializable {
    private String accessType;
    private String accessDescription;

    @Override
    public String getId() {
        return accessType;
    }
}
