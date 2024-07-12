package bg.duosoft.uniapplicationdemo.controllers.base;

import bg.duosoft.uniapplicationdemo.exceptions.ForbiddenException;
import bg.duosoft.uniapplicationdemo.exceptions.UnauthorizedException;
import bg.duosoft.uniapplicationdemo.security.RolesBuilder;
import bg.duosoft.uniapplicationdemo.security.SecurityRole;
import bg.duosoft.uniapplicationdemo.security.SecurityUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class BaseAccessController {
    public String getReadOnlyRole() {
        return SecurityRole.READ_ONLY_ACCESS;
    }

    public String getAccessRole() {
        return SecurityRole.FULL_ACCESS;
    }

    public String getAdminRole() {
        return SecurityRole.ADMIN_ACCESS;
    }

    public Boolean isProtected() {
        return true;
    }

    public static void checkPermissions(String roles) {
        if (!SecurityUtils.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }

        if (Objects.nonNull(roles)) {
            List<String> roleList = splitRoles(roles, roles.contains("-or-") ? RolesBuilder.OR_OPERATOR : RolesBuilder.AND_OPERATOR);
            if (roleList.size() == 1) {
                if (!SecurityUtils.hasRole(roleList.get(0))) {
                    throw new ForbiddenException();
                }
            } else {
                if (roles.contains("-and") && (!SecurityUtils.hasRole(roleList.get(0)) || !SecurityUtils.hasRole(roleList.get(1)))) {
                    throw new ForbiddenException();
                } else if (roles.contains("-or-") && !SecurityUtils.hasAnyRole(roleList)) {
                    throw new ForbiddenException();
                }
            }
        }
    }

    private static List<String> splitRoles(String roles, String operator) {
        return Arrays.stream(roles.split(operator))
                .filter(Objects::nonNull)
                .map(String::trim)
                .toList();
    }
}
