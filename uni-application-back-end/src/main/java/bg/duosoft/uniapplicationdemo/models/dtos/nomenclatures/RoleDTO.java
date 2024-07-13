package bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
public class RoleDTO extends BaseDTO<String> implements Serializable {
    private String role;

    private String roleDescription;

    @Override
    public String getId() {
        return role;
    }
}
