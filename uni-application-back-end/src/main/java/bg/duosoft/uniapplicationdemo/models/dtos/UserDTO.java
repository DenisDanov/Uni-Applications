package bg.duosoft.uniapplicationdemo.models.dtos;

import bg.duosoft.uniapplicationdemo.models.dtos.base.BaseDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.AccessLevelDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends BaseDTO<String> implements Serializable {
    private String username;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String password;

    private Date dateOfBirth;

    private String phoneNumber;

    private AccessLevelDTO accessLevel;

    private RoleDTO roleDTO;

    private Boolean enabled;

    @Override
    public String getId() {
        return username;
    }
}
