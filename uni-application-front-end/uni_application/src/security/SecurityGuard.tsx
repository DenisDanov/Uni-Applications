import React from "react";
import {HasAccessParams} from "../types/HasAccessParams";
import {WithChildren} from "../types";
import {useKeycloak} from "../keycloak";
import {hasAccess} from "./HasAccess";

export type SecurityGuardProps = HasAccessParams & {
    displayOnUnauthorized?: React.ReactNode;
};

const SecurityGuard = ({
                           children,
                           doCheck = true,
                           checkForRoles = [],
                           roleOperator = "and",
                           displayOnUnauthorized,
                           loginOnUnauthorized = false,
                       }: WithChildren<SecurityGuardProps>) => {
    const { initialized, keycloak } = useKeycloak();

    let allowAccess = hasAccess({ doCheck, loginOnUnauthorized, checkForRoles, roleOperator }, keycloak, initialized);

    if (allowAccess) {
        return <React.Fragment>{children}</React.Fragment>;
    } else if (displayOnUnauthorized) {
        return <React.Fragment>{displayOnUnauthorized}</React.Fragment>;
    } else if (loginOnUnauthorized) {
        keycloak.login();
        return null;
    } else {
        return null;
    }
};

export default SecurityGuard;