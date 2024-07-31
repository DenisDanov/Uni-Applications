package bg.duosoft.uniapplicationdemo.services;

import bg.duosoft.uniapplicationdemo.models.dtos.FilterUsersDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.UserDTO;
import bg.duosoft.uniapplicationdemo.models.dtos.nomenclatures.RoleDTO;
import bg.duosoft.uniapplicationdemo.services.impl.KeycloakUserServiceImpl;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class KeycloakUserServiceImplTest {

    private Keycloak keycloak;
    private KeycloakUserServiceImpl keycloakUserService;
    private UsersResource usersResource;
    private RealmResource realmResource;
    private UserResource userResource;

    @BeforeEach
    void setUp() {
        keycloak = mock(Keycloak.class);
        realmResource = mock(RealmResource.class);
        usersResource = mock(UsersResource.class);
        userResource = mock(UserResource.class);
        String realm = "SpringRealm";

        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);

        keycloakUserService = new KeycloakUserServiceImpl(keycloak, realm);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        UserRepresentation user1 = new UserRepresentation();
        user1.setUsername("user1");
        UserRepresentation user2 = new UserRepresentation();
        user2.setUsername("user2");
        List<UserRepresentation> userList = Arrays.asList(user1, user2);

        when(usersResource.list()).thenReturn(userList);

        // Act
        List<UserRepresentation> users = keycloakUserService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(usersResource).list();
    }

    @Test
    void testGetUserByUsername() {
        // Arrange
        UserRepresentation user = new UserRepresentation();
        user.setUsername("testUser");
        List<UserRepresentation> userList = Collections.singletonList(user);

        when(usersResource.search(anyString())).thenReturn(userList);

        // Act
        UserRepresentation foundUser = keycloakUserService.getUserByUsername("testUser");

        // Assert
        assertEquals("testUser", foundUser.getUsername());
        verify(usersResource).search("testUser");
    }

    @Test
    void testGetUserByUsernameNotFound() {
        // Arrange
        when(usersResource.search(anyString())).thenReturn(Collections.emptyList());

        // Act
        UserRepresentation foundUser = keycloakUserService.getUserByUsername("nonExistentUser");

        // Assert
        assertNull(foundUser);
        verify(usersResource).search("nonExistentUser");
    }

    @Test
    void testCreateUserFailure() {
        // Arrange
        String username = "newUser";
        String email = "email@example.com";
        String firstName = "First";
        String middleName = "Middle";
        String lastName = "Last";
        String password = "password";
        Date dateOfBirth = Date.valueOf("2000-01-01");
        String phoneNumber = "1234567890";
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole("student");

        Response response = mock(Response.class);
        when(response.getStatus()).thenReturn(400); // Simulate failure response

        when(usersResource.create(any(UserRepresentation.class))).thenReturn(response);

        // Act
        Response result = keycloakUserService.createUser(
                username, email, firstName, middleName, lastName, password, dateOfBirth, phoneNumber, roleDTO);

        // Assert
        assertEquals(400, result.getStatus());
        verify(usersResource).create(any(UserRepresentation.class));
        verifyNoInteractions(userResource); // No interactions expected if creation fails
    }

    @Test
    void testFilterUsers() {
        // Arrange
        UserRepresentation user1 = new UserRepresentation();
        user1.setUsername("alice");
        user1.setEmail("alice@example.com");
        user1.setAttributes(createAttributes("1234567890"));

        UserRepresentation user2 = new UserRepresentation();
        user2.setUsername("bob");
        user2.setEmail("bob@example.com");
        user2.setAttributes(createAttributes("0987654321"));

        UserRepresentation user3 = new UserRepresentation();
        user3.setUsername("carol");
        user3.setEmail("carol@example.com");
        user3.setAttributes(createAttributes("1234567890"));

        List<UserRepresentation> users = Arrays.asList(user1, user2, user3);
        when(usersResource.list()).thenReturn(users);

        FilterUsersDTO filterUsersDTO = new FilterUsersDTO();
        filterUsersDTO.setUsername("alice");
        filterUsersDTO.setEmail(null);
        filterUsersDTO.setPhoneNumber(null);
        filterUsersDTO.setMaxResults(2);

        // Act
        List<UserRepresentation> result = keycloakUserService.filterUsers(filterUsersDTO);

        // Assert
        assertEquals(1, result.size());
        assertEquals("alice", result.get(0).getUsername());

        // Test with additional filters
        filterUsersDTO.setUsername(null);
        filterUsersDTO.setEmail("bob@example.com");
        filterUsersDTO.setPhoneNumber(null);
        result = keycloakUserService.filterUsers(filterUsersDTO);
        assertEquals(1, result.size());
        assertEquals("bob", result.get(0).getUsername());

        filterUsersDTO.setUsername(null);
        filterUsersDTO.setEmail(null);
        filterUsersDTO.setPhoneNumber("1234567890");
        result = keycloakUserService.filterUsers(filterUsersDTO);
        assertEquals(2, result.size());

        filterUsersDTO.setMaxResults(1);
        result = keycloakUserService.filterUsers(filterUsersDTO);
        assertEquals(1, result.size());
        assertEquals("alice", result.get(0).getUsername());
    }

    @Test
    void testDeleteByUsernameSuccess() {
        // Arrange
        UserRepresentation user = new UserRepresentation();
        user.setId("user-id");
        user.setUsername("testUser");
        List<UserRepresentation> userList = Collections.singletonList(user);

        when(usersResource.search("testUser")).thenReturn(userList);
        UserResource userResource = mock(UserResource.class);
        when(usersResource.get("user-id")).thenReturn(userResource);

        // Act
        keycloakUserService.deleteByUsername("testUser");

        // Assert
        verify(usersResource).search("testUser");
        verify(usersResource).get("user-id");
        verify(userResource).remove();
    }

    @Test
    void testDeleteByUsernameNotFound() {
        // Arrange
        when(usersResource.search("nonExistentUser")).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                keycloakUserService.deleteByUsername("nonExistentUser"));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testDeleteByUsernameFailure() {
        // Arrange
        UserRepresentation user = new UserRepresentation();
        user.setId("user-id");
        user.setUsername("testUser");
        List<UserRepresentation> userList = Collections.singletonList(user);

        when(usersResource.search("testUser")).thenReturn(userList);
        UserResource userResource = mock(UserResource.class);
        when(usersResource.get("user-id")).thenReturn(userResource);
        doThrow(new RuntimeException("Deletion failed")).when(userResource).remove();

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                keycloakUserService.deleteByUsername("testUser"));
        assertEquals("Failed to delete user", thrown.getMessage());
    }

    @Test
    void testGetRealmRoles() {
        // Arrange
        RoleRepresentation studentRole = new RoleRepresentation();
        RoleRepresentation adminRole = new RoleRepresentation();
        RoleRepresentation readOnlyAccessRole = new RoleRepresentation();
        RoleRepresentation fullAccessRole = new RoleRepresentation();

        // Mock RoleResource
        org.keycloak.admin.client.resource.RoleResource studentRoleResource = mock(org.keycloak.admin.client.resource.RoleResource.class);
        org.keycloak.admin.client.resource.RoleResource adminRoleResource = mock(org.keycloak.admin.client.resource.RoleResource.class);
        org.keycloak.admin.client.resource.RoleResource readOnlyAccessRoleResource = mock(org.keycloak.admin.client.resource.RoleResource.class);
        org.keycloak.admin.client.resource.RoleResource fullAccessRoleResource = mock(org.keycloak.admin.client.resource.RoleResource.class);

        // Mock RolesResource
        org.keycloak.admin.client.resource.RolesResource rolesResource = mock(org.keycloak.admin.client.resource.RolesResource.class);

        // Set up stubbing for rolesResource.get(roleName)
        when(rolesResource.get("student")).thenReturn(studentRoleResource);
        when(rolesResource.get("admin")).thenReturn(adminRoleResource);
        when(rolesResource.get("READ_ONLY_ACCESS")).thenReturn(readOnlyAccessRoleResource);
        when(rolesResource.get("FULL_ACCESS")).thenReturn(fullAccessRoleResource);

        // Set up stubbing for roleResource.toRepresentation()
        when(studentRoleResource.toRepresentation()).thenReturn(studentRole);
        when(adminRoleResource.toRepresentation()).thenReturn(adminRole);
        when(readOnlyAccessRoleResource.toRepresentation()).thenReturn(readOnlyAccessRole);
        when(fullAccessRoleResource.toRepresentation()).thenReturn(fullAccessRole);

        // Set up stubbing for keycloak.realm().roles()
        when(keycloak.realm(anyString()).roles()).thenReturn(rolesResource);

        // Act
        List<RoleRepresentation> roles = keycloakUserService.getRealmRoles();

        // Assert
        assertEquals(4, roles.size());
        assertTrue(roles.contains(studentRole));
        assertTrue(roles.contains(adminRole));
        assertTrue(roles.contains(readOnlyAccessRole));
        assertTrue(roles.contains(fullAccessRole));
    }

    private Map<String, List<String>> createAttributes(String phoneNumber) {
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phoneNumber", Collections.singletonList(phoneNumber));
        return attributes;
    }

    @Test
    void testGetUserByEmail() {
        // Arrange
        UserRepresentation user = new UserRepresentation();
        user.setEmail("test@example.com");
        List<UserRepresentation> userList = Collections.singletonList(user);

        when(usersResource.searchByEmail(anyString(), anyBoolean())).thenReturn(userList);

        // Act
        UserRepresentation foundUser = keycloakUserService.getUserByEmail("test@example.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(usersResource).searchByEmail("test@example.com", true);
    }

    @Test
    void testGetUserByEmailNotFound() {
        // Arrange
        when(usersResource.searchByEmail(anyString(), anyBoolean())).thenReturn(Collections.emptyList());

        // Act
        UserRepresentation foundUser = keycloakUserService.getUserByEmail("nonexistent@example.com");

        // Assert
        assertNull(foundUser);
        verify(usersResource).searchByEmail("nonexistent@example.com", true);
    }

    @Test
    void testManageBlockingUserBlock() {
        // Arrange
        UserRepresentation user = mock(UserRepresentation.class);
        when(usersResource.search("testUser")).thenReturn(Collections.singletonList(user));
        when(usersResource.get(user.getId())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(user);
        when(user.isEnabled()).thenReturn(true);

        // Act
        boolean result = keycloakUserService.manageBlockingUser("testUser", "block");

        // Assert
        assertTrue(result);
        verify(userResource).update(user);
    }

    @Test
    void testManageBlockingUserUnblock() {
        // Arrange
        UserRepresentation user = mock(UserRepresentation.class);
        when(usersResource.search("testUser")).thenReturn(Collections.singletonList(user));
        when(usersResource.get(user.getId())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(user);
        when(user.isEnabled()).thenReturn(false);

        // Act
        boolean result = keycloakUserService.manageBlockingUser("testUser", "unblock");

        // Assert
        assertTrue(result);
        verify(userResource).update(user);
    }

    @Test
    void testManageBlockingUserNoChange() {
        // Arrange
        UserRepresentation user = mock(UserRepresentation.class);
        when(usersResource.search("testUser")).thenReturn(Collections.singletonList(user));
        when(usersResource.get(user.getId())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(user);
        when(user.isEnabled()).thenReturn(true);

        // Act
        boolean result = keycloakUserService.manageBlockingUser("testUser", "unblock");

        // Assert
        assertFalse(result); // Expecting no change to be made
        assertTrue(user.isEnabled()); // User should remain enabled
        verify(userResource, never()).update(any(UserRepresentation.class));
    }

    @Test
    void testDeleteByIdSuccess() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");

        UserRepresentation user = new UserRepresentation();
        user.setId("user-id");
        user.setUsername("testUser");
        List<UserRepresentation> userList = Collections.singletonList(user);

        when(usersResource.search("testUser")).thenReturn(userList);
        when(usersResource.get("user-id")).thenReturn(userResource);

        // Act
        keycloakUserService.deleteById(userDTO.getUsername());

        // Assert
        verify(usersResource).search("testUser");
        verify(usersResource).get("user-id");
        verify(userResource).remove();
    }

    @Test
    void testDeleteByIdNotFound() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("nonExistentUser");

        when(usersResource.search("nonExistentUser")).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () ->
                keycloakUserService.delete(userDTO));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testUpdateUser() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setMiddleName("Michael");
        userDTO.setDateOfBirth(java.sql.Date.valueOf("1990-01-01"));
        userDTO.setPhoneNumber("1234567890");

        UserRepresentation existingUserRepresentation = new UserRepresentation();
        existingUserRepresentation.setAttributes(new HashMap<>());

        when(usersResource.search(userDTO.getUsername().toLowerCase())).thenReturn(Collections.singletonList(existingUserRepresentation));
        when(usersResource.get(existingUserRepresentation.getId())).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(existingUserRepresentation);

        // Act
        UserDTO updatedUserDTO = keycloakUserService.update(userDTO);

        // Assert
        assertEquals(userDTO, updatedUserDTO);
        verify(userResource).update(existingUserRepresentation);

        // Verify attributes are set correctly
        Map<String, List<String>> expectedAttributes = new HashMap<>();
        expectedAttributes.put("middleName", Collections.singletonList("Michael"));
        expectedAttributes.put("dateOfBirth", Collections.singletonList("1990-01-01"));
        expectedAttributes.put("phoneNumber", Collections.singletonList("1234567890"));

        assertEquals(expectedAttributes, existingUserRepresentation.getAttributes());
        assertEquals("John", existingUserRepresentation.getFirstName());
        assertEquals("Doe", existingUserRepresentation.getLastName());
        assertEquals("john.doe@example.com", existingUserRepresentation.getEmail());
    }

    @Test
    void testUpdateUserNotFound() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("nonExistentUser");

        when(usersResource.search(anyString())).thenReturn(Collections.emptyList());

        // Act & Assert
        NoSuchElementException thrown = assertThrows(NoSuchElementException.class, () ->
                keycloakUserService.update(userDTO));
        assertEquals(null, thrown.getMessage());
    }
}
