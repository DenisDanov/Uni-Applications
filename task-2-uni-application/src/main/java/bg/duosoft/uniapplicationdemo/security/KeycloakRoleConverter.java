package bg.duosoft.uniapplicationdemo.security;

import bg.duosoft.uniapplicationdemo.exceptions.ReadClaimException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess;
        try {
            realmAccess = (Map<String, Object>) jwt.getClaims().get(REALM_ACCESS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReadClaimException(REALM_ACCESS, jwt.getTokenValue());
        }

        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<GrantedAuthority> roles = ((List<String>) realmAccess.get(ROLES))
                .stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return roles;
    }


}