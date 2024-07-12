import {KeycloakInstance} from "keycloak-js";
import {HasAccessParams} from "../types/HasAccessParams";

export const hasAccess = (
    {doCheck, loginOnUnauthorized, checkForRoles, roleOperator}: HasAccessParams,
    keycloak: KeycloakInstance,
    initialized: boolean
): boolean => {
    let allowAccess = false;

    if (!doCheck) {
        allowAccess = true;
    } else {
        if (initialized && keycloak.authenticated) {
            // @ts-ignore
            if (checkForRoles.length === 0) {
                allowAccess = true;
            } else {
                // @ts-ignore
                const hasProperRoles = checkForRoles
                    .map((role) => keycloak.hasRealmRole(role))
                    .reduce((r1, r2) => {
                        if (roleOperator === "and") {
                            return r1 && r2;
                        } else if (roleOperator === "or") {
                            return r1 || r2;
                        } else {
                            console.warn(`Unexpected roleOperator value: ${roleOperator}. Defaulting to "or" behavior.`);
                            return r1 || r2;
                        }
                    }, roleOperator === "and");

                if (hasProperRoles) {
                    allowAccess = true;
                }
            }
        }
    }

    return allowAccess;
};
