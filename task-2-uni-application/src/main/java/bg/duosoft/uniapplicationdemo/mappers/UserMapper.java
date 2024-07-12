package bg.duosoft.uniapplicationdemo.mappers;

import bg.duosoft.uniapplicationdemo.mappers.base.BaseObjectMapper;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.services.AccessLevelService;
import bg.duosoft.uniapplicationdemo.services.RoleService;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

@Mapper(componentModel = "spring")
public abstract class UserMapper extends BaseObjectMapper<UserRepresentation, UserDTO> {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccessLevelService accessLevelService;

    @AfterMapping
    protected void afterToDTOMapping(UserRepresentation userRepresentation,
                                     @MappingTarget UserDTO userDTO) {
        userDTO.setMiddleName(userRepresentation.getAttributes().get("middleName").get(0));
        if (userRepresentation.getAttributes().get("phoneNumber") != null) {
            userDTO.setPhoneNumber(userRepresentation.getAttributes().get("phoneNumber").get(0));
        }
        if (userRepresentation.getAttributes().get("dateOfBirth") != null) {
            userDTO.setDateOfBirth(Date.valueOf(userRepresentation.getAttributes().get("dateOfBirth").get(0)));
        }
        userDTO.setRoleDTO(roleService.getAll().stream().filter(roleDTO -> roleDTO.getRole().equals(userRepresentation.getAttributes().get("role").get(0).toUpperCase())).findFirst().get());
        userDTO.setEnabled(userRepresentation.isEnabled());
        if (userRepresentation.getAttributes().get("accessLevel") != null) {
            userDTO.setAccessLevel(accessLevelService.getAll().stream().filter(accessLevelDTO -> accessLevelDTO.getAccessType().equals(userRepresentation.getAttributes().get("accessLevel").get(0).toUpperCase())).findFirst().get());
        }
    }

}
