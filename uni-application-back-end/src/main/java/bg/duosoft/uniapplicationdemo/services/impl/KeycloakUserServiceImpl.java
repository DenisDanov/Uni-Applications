package bg.duosoft.uniapplicationdemo.services.impl;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.services.KeycloakUserService;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.*;

@Service
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private final String realm;

    private static final Logger logger = LoggerFactory.getLogger(KeycloakUserServiceImpl.class);

    public KeycloakUserServiceImpl(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public Response createUser(String username, String email, String firstName, String middleName, String lastName, String password, Date dateOfBirth, String phoneNumber, RoleDTO roleDTO) {
        UsersResource usersResource = keycloak.realm(realm).users();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        Map<String, List<String>> attributes = new HashMap<>() {{
            put("middleName", Collections.singletonList(middleName));
            put("dateOfBirth", Collections.singletonList(dateOfBirth.toString()));
            put("phoneNumber", Collections.singletonList(phoneNumber));
            put("role", Collections.singletonList(roleDTO.getRole().toLowerCase()));
        }};
        user.setAttributes(attributes);

        // Create the user in Keycloak
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            String userId = CreatedResponseUtil.getCreatedId(response);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            usersResource.get(userId).resetPassword(credential);

            // Assign the student role
            RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(roleDTO.getRole().toLowerCase()).toRepresentation();
            RoleRepresentation studentRoleRepresentation = keycloak.realm(realm).roles().get("student").toRepresentation();

            usersResource.get(userId).roles().realmLevel().add(Arrays.asList(roleRepresentation, studentRoleRepresentation));
        }

        return response;
    }

    @Override
    public List<UserRepresentation> getAllUsers() {
        logger.info("Get all users");
        logger.debug("Getting all users");
        UsersResource usersResource = keycloak.realm(realm).users();
        logger.info("Got all users");
        return usersResource.list();
    }

    @Override
    public UserRepresentation getUserByUsername(String username) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.search(username);
        if (!users.isEmpty()) {
            return users.stream().filter(userRepresentation -> userRepresentation.getUsername().equals(username)).findFirst().get();
        }
        return null;
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        UsersResource usersResource = keycloak.realm(realm).users();

        UserResource userResource = usersResource.get(usersResource.search(userDTO.getUsername()).getFirst().getId());
        UserRepresentation userRepresentation = userResource.toRepresentation();

        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setEmail(userDTO.getEmail());

        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        attributes.put("middleName", Collections.singletonList(userDTO.getMiddleName()));
        if (userDTO.getDateOfBirth() != null) {
            attributes.put("dateOfBirth", Collections.singletonList(userDTO.getDateOfBirth().toString()));
        }
        if (userDTO.getPhoneNumber() != null) {
            attributes.put("phoneNumber", Collections.singletonList(userDTO.getPhoneNumber()));
        }
        userRepresentation.setAttributes(attributes);

        userResource.update(userRepresentation);

        return userDTO;
    }

    @Override
    public UserDTO updateUserRoles(UserDTO userDTO) {
        UsersResource usersResource = keycloak.realm(realm).users();

        UserResource userResource = usersResource.get(usersResource.search(userDTO.getUsername()).getFirst().getId());
        UserRepresentation userRepresentation = userResource.toRepresentation();
        usersResource.get(userRepresentation.getId()).roles().realmLevel().remove(getRealmRoles());
        userRepresentation.getAttributes().put("accessLevel", Collections.singletonList(""));

        String lowerCaseRole = userDTO.getRoleDTO().getRole().toLowerCase();

        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        attributes.put("role", Collections.singletonList(lowerCaseRole));
        if (Objects.nonNull(userDTO.getAccessLevel()) && Objects.nonNull(userDTO.getAccessLevel().getAccessType())) {
            String accessType = userDTO.getAccessLevel().getAccessType();

            attributes.put("accessLevel", Collections.singletonList(accessType));
            attributes.put("phoneNumber", Collections.emptyList());
            attributes.put("dateOfBirth", Collections.emptyList());
            userDTO.setPhoneNumber(null);
            userDTO.setDateOfBirth(null);

            RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(accessType).toRepresentation();
            RoleRepresentation roleTypeRepresentation = keycloak.realm(realm).roles().get(accessType).toRepresentation();

            usersResource.get(userRepresentation.getId()).roles().realmLevel().add(Arrays.asList(roleRepresentation, roleTypeRepresentation));
            userRepresentation.setAttributes(attributes);
        }
        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(lowerCaseRole).toRepresentation();
        RoleRepresentation roleTypeRepresentation = keycloak.realm(realm).roles().get(lowerCaseRole).toRepresentation();

        usersResource.get(userRepresentation.getId()).roles().realmLevel().add(Arrays.asList(roleRepresentation, roleTypeRepresentation));
        userRepresentation.setAttributes(attributes);

        userResource.update(userRepresentation);

        return userDTO;
    }

    @Override
    public List<UserRepresentation> filterUsers(FilterUsersDTO filterUsersDTO) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.list();

        String username = filterUsersDTO.getUsername();
        String email = filterUsersDTO.getEmail();
        String phoneNumber = filterUsersDTO.getPhoneNumber();

        boolean filterByUsername = StringUtils.hasText(username);
        boolean filterByEmail = StringUtils.hasText(email);
        boolean filterByPhoneNumber = StringUtils.hasText(phoneNumber);

        List<UserRepresentation> filteredUsers = users.stream()
                .filter(user -> {
                    boolean match = true;

                    if (filterByUsername) {
                        match &= user.getUsername().equalsIgnoreCase(username);
                    }
                    if (filterByEmail) {
                        match &= user.getEmail().equalsIgnoreCase(email);
                    }
                    if (filterByPhoneNumber) {
                        match &= user.getAttributes() != null &&
                                user.getAttributes().containsKey("phoneNumber") &&
                                user.getAttributes().get("phoneNumber").contains(phoneNumber);
                    }

                    return match;
                })
                .toList();

        List<UserRepresentation> finalFilteredUsers = new ArrayList<>(filteredUsers);

        // Apply maxResults limit if specified
        int maxResults = filterUsersDTO.getMaxResults();
        if (maxResults >= 1 && maxResults < finalFilteredUsers.size()) {
            return finalFilteredUsers.subList(0, maxResults);
        }

        return finalFilteredUsers;
    }

    @Override
    public void delete(UserDTO userDTO) {
        deleteByUsername(userDTO.getUsername());
    }

    @Override
    public void deleteById(String username) {
        deleteByUsername(username);
    }

    @Override
    public UserRepresentation getUserByEmail(String email) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.searchByEmail(email, true);
        if (!users.isEmpty()) {
            return users.stream().filter(userRepresentation -> userRepresentation.getEmail().equals(email)).findFirst().get();
        }
        return null;
    }

    @Override
    public boolean manageBlockingUser(String username, String blockStatus) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        UserResource userResource = usersResource.get(usersResource.search(username).getFirst().getId());
        UserRepresentation userRepresentation = userResource.toRepresentation();

        boolean shouldBlock = blockStatus.equals("block");
        boolean isUserEnabled = userRepresentation.isEnabled();

        if (isUserEnabled == shouldBlock) {
            userRepresentation.setEnabled(!isUserEnabled);
            userResource.update(userRepresentation);
            return true;
        }

        return false;
    }

    private void deleteByUsername(String username) {
        UsersResource usersResource = keycloak.realm(realm).users();
        List<UserRepresentation> users = usersResource.search(username);

        if (users.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        try {
            usersResource.get(users.getFirst().getId()).remove();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    private List<RoleRepresentation> getRealmRoles() {
        return List.of(keycloak.realm(realm).roles().get("student").toRepresentation(),
                keycloak.realm(realm).roles().get("admin").toRepresentation(),
                keycloak.realm(realm).roles().get("READ_ONLY_ACCESS").toRepresentation(),
                keycloak.realm(realm).roles().get("FULL_ACCESS").toRepresentation());
    }
}
