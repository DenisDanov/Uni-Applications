package bg.duosoft.uniapplicationdemo.security;

import bg.duosoft.uniapplicationdemo.exceptions.ForbiddenException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUtils {

    public static final String USERNAME_CLAIM = "preferred_username";
    public static final String FIRST_NAME_CLAIM = "given_name";
    public static final String LAST_NAME_CLAIM = "family_name";
    public static final String EMAIL_CLAIM = "email";
    public static final String SUB_CLAIM = "sub";

    public static boolean isUserAuthenticated() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Jwt) {
            return true;
        }
        return false;
    }

    public static Object getClaim(String claim) {
        if (isUserAuthenticated()) {
            Jwt jwt = getJwt();
            if (jwt != null) {
                return jwt.getClaims().get(claim);
            }
        }
        return null;
    }

    public static boolean hasRole(String roleName) {
        if (isUserAuthenticated()) {
            GrantedAuthority existing = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().filter(granted -> granted.getAuthority().equals(roleName) || granted.getAuthority().equals("ROLE_" + roleName)).findFirst().orElse(null);
            if (existing != null) {
                return true;
            }
        }
        return false;
    }

    public static String getUsername() {
        return (String) getClaim(USERNAME_CLAIM);
    }

    public static String getEmail() {
        return (String) getClaim(EMAIL_CLAIM);
    }

    public static String getAccessToken() {
        if (isUserAuthenticated()) {
            return getJwt().getTokenValue();
        }
        return null;
    }

    public static Jwt getJwt() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static List<String> getLoggedUserRoles() {
        if (!isUserAuthenticated()) {
            return null;
        }

        KeycloakRoleConverter converter = new KeycloakRoleConverter();
        Collection<GrantedAuthority> loggedUserAuthorities = converter.convert(getJwt());
        if (CollectionUtils.isEmpty(loggedUserAuthorities)) {
            return null;
        }

        List<String> result = new ArrayList<>();
        for (GrantedAuthority loggedUserAuthority : loggedUserAuthorities) {
            String authString = loggedUserAuthority.getAuthority();
            if (StringUtils.hasText(authString) && authString.startsWith("ROLE_")) {
                String roleWithoutPrefix = authString.replaceFirst("^" + "ROLE_", "");
                result.add(roleWithoutPrefix);
            }
        }
        return result;
    }

    public static boolean hasAnyRole(List<String> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return false;
        }

        List<String> userRoles = getLoggedUserRoles();
        if (CollectionUtils.isEmpty(userRoles)) {
            return false;
        }

        return CollectionUtils.containsAny(userRoles, roles);
    }

    public static void validateAnyRole(List<String> roles) {
        boolean hasAnyRole = hasAnyRole(roles);
        if (!hasAnyRole) {
            throw new ForbiddenException("Access denied!");
        }
    }

    public static BaseUserDetails getAccessTokenUserDetails() {
        if (isUserAuthenticated()) {
            BaseUserDetails userDetails = new BaseUserDetails(
                    (String) getClaim(SUB_CLAIM),
                    getUsername(),
                    (String) getClaim(FIRST_NAME_CLAIM),
                    (String) getClaim(LAST_NAME_CLAIM),
                    getEmail());
            return userDetails;
        }
        return null;
    }
}
