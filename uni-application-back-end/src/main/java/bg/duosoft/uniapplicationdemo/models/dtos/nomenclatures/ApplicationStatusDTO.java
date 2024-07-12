package bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.*;

import java.io.Serializable;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplicationStatusDTO extends BaseDTO<String> implements Serializable {
    private String applicationStatus;

    private String applicationDescription;

    @Override
    public String getId() {
        return applicationStatus;
    }
}
